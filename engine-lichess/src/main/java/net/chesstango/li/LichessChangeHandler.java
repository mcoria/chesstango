package net.chesstango.li;

import chariot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class LichessChangeHandler {
    private static final Logger logger = LoggerFactory.getLogger(LichessBotMain.class);
    private final LichessClient client;
    private final int maxSimultaneousGames;
    private final Map<String, LichessTango> onlineGameMap;
    private boolean acceptChallenges;

    public LichessChangeHandler(LichessClient client, int maxSimultaneousGames, Map<String, LichessTango> onlineGameMap) {
        this.client = client;
        this.maxSimultaneousGames = maxSimultaneousGames;
        this.onlineGameMap = onlineGameMap;
        this.acceptChallenges = true;
    }


    public void newChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("New challenge received: {}", challengeEvent.id());

        if (acceptChallenges) {
            if (isChallengeAcceptable(challengeEvent)) {
                acceptChallenge(challengeEvent);
            } else {
                declineChallenge(challengeEvent);
            }
        } else {
            logger.info("Not accepting more challenges at this time {}", challengeEvent.id());
            declineChallenge(challengeEvent);
        }
    }

    public void stopAcceptingChallenges() {
        this.acceptChallenges = false;
    }

    private void acceptChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Accepting challenge {}", challengeEvent.id());

        client.challengeAccept(challengeEvent.id());

    }

    private void declineChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Declining challenge {}", challengeEvent.id());
        client.challengeDecline(challengeEvent.id());
    }


    private boolean isChallengeAcceptable(Event.ChallengeEvent challengeEvent) {
        Optional<ChallengeInfo.Player> challengerPlayer = challengeEvent.challenge().players().challengerOpt();
        Optional<ChallengeInfo.Player> challengedPlayer = challengeEvent.challenge().players().challengedOpt();

        if (challengerPlayer.isEmpty() || challengedPlayer.isEmpty()) {
            return false;
        } else if (client.isMe(challengerPlayer.get().user())) { // Siempre acepto mis propios challenges
            return true;
        }


        GameType gameType = challengeEvent.challenge().gameType();
        long timeControlledGames = onlineGameMap.values()
                .stream()
                .filter(LichessTango::isTimeControlledGame)
                .count();

        return isVariantAcceptable(gameType.variant())                // Chess variant
                && isTimeControlAcceptable(gameType.timeControl())        // Time control
                && timeControlledGames < maxSimultaneousGames          // Not busy..
                && isChallengerAcceptable(challengerPlayer.get(), gameType.timeControl().speed());
    }

    private boolean isChallengerAcceptable(ChallengeInfo.Player player, Enums.Speed speed) {
        if (player.user().titleOpt().isEmpty()) { // Quiere decir que es human - aceptamos en todos los casos
            return true;
        }

        String userTitle = player.user().titleOpt().get();

        StatsPerfType statsPerfType = switch (speed) {
            case bullet -> StatsPerfType.bullet;
            case blitz -> StatsPerfType.blitz;
            case rapid -> StatsPerfType.rapid;
            default -> null;
        };

        if (statsPerfType == null) {
            return false;
        }

        int myRating = client.getRating(statsPerfType);

        return "BOT".equals(userTitle) && player.rating() <= myRating + LichessChallengerBot.RATING_THRESHOLD;
    }

    private static boolean isVariantAcceptable(VariantType variant) {
        return VariantType.Variant.standard.equals(variant) || variant instanceof VariantType.FromPosition;
    }

    private static boolean isTimeControlAcceptable(TimeControl timeControl) {
        Predicate<RealTime> supportedRealtimeGames = realtime ->
                (Enums.Speed.bullet.equals(realtime.speed())
                        || Enums.Speed.blitz.equals(realtime.speed())
                        || Enums.Speed.rapid.equals(realtime.speed()))
                        && realtime.initial().getSeconds() >= 30L;


        return //timeControl instanceof Unlimited ||                                                   // Unlimited games x el momento no soportados
                (timeControl instanceof RealTime realtime && supportedRealtimeGames.test(realtime));   // Realtime

    }

}
