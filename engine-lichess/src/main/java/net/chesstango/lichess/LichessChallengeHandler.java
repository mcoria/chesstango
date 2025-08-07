package net.chesstango.lichess;

import chariot.model.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessChallengeHandler {
    private final LichessClient client;

    private final Supplier<Boolean> fnIsBusy;

    private boolean acceptChallenges;

    public LichessChallengeHandler(LichessClient client, Supplier<Boolean> fnIsBusy) {
        this.client = client;
        this.fnIsBusy = fnIsBusy;
        this.acceptChallenges = true;
    }


    public void challengeCreated(Event.ChallengeCreatedEvent event) {
        log.info("ChallengeCreatedEvent: {}", event.id());
        if (acceptChallenges) {
            if (isChallengeAcceptable(event)) {
                acceptChallenge(event);
            } else {
                declineChallenge(event);
            }
        } else {
            log.info("Not accepting more challenges at this time {}", event.id());
            declineChallenge(event);
        }
    }

    public void challengeCanceled(Event.ChallengeCanceledEvent event) {
        log.info("ChallengeCanceledEvent: {}", event.id());
    }

    public void challengeDeclined(Event.ChallengeDeclinedEvent event) {
        log.info("ChallengeDeclinedEvent: {}", event.id());
    }

    public void stopAcceptingChallenges() {
        this.acceptChallenges = false;
    }

    private void acceptChallenge(Event.ChallengeEvent challengeEvent) {
        log.info("Accepting challenge {}", challengeEvent.id());
        client.challengeAccept(challengeEvent.id());
    }

    private void declineChallenge(Event.ChallengeEvent challengeEvent) {
        log.info("Declining challenge {}", challengeEvent.id());
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

        return isVariantAcceptable(gameType.variant())                          // Chess variant
                && isTimeControlAcceptable(gameType.timeControl())              // Time control
                && isChallengerAcceptable(challengerPlayer.get(), gameType.timeControl().speed())
                && !fnIsBusy.get();                                             // Not busy..
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

    private static boolean isVariantAcceptable(Variant variant) {
        return Variant.Basic.standard.equals(variant) || variant instanceof Variant.FromPosition;
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
