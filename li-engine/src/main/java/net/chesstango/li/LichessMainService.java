package net.chesstango.li;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessMainService implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(LichessMainService.class);

    public static void main(String[] args) {
        URI lichessApi = URI.create("https://lichess.org");

        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), "lip_Qk9igzwz1rgEWOUOKf6P");

        logger.info("Start playing as a bot");

        new LichessMainService(new LichessClient(clientAuth)).run();
    }

    private final Map<String, LichessGame> onlineGameMap = new HashMap<>();

    private final LichessClient client;

    public LichessMainService(LichessClient client) {
        this.client = client;
    }

    @Override
    public void run() {
        Stream<Event> events = client.streamEvents();

        logger.info("Connection successful, waiting for challenges...");

        events.forEach(event -> {
            switch (event.type()) {
                case challenge -> newChallenge((Event.ChallengeEvent)event);
                case challengeCanceled, challengeDeclined -> logger.info("Challenge cancelled / declined: {}", event);
                case gameStart -> startGame((Event.GameStartEvent)event);
                case gameFinish -> gameFinish((Event.GameStopEvent)event);
            }
        });
    }

    private void newChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("New challenge received. Details: {}", challengeEvent.challenge());

        if (isChallengeAcceptable(challengeEvent)) {
            acceptChallenge(challengeEvent);
        } else {
            declineChallenge(challengeEvent);
        }
    }

    private boolean isChallengeAcceptable(Event.ChallengeEvent challengeEvent) {
        Predicate<ChallengeInfo> variantStandard = challenge -> VariantType.Variant.standard.equals(challenge.gameType().variant()) && challenge.gameType().timeControl() instanceof Unlimited;

        return variantStandard.test(challengeEvent.challenge());
    }

    private void acceptChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Accepting challenge {}!", challengeEvent.id());

        var onlineGame = new LichessGame(client, challengeEvent.id(), challengeEvent.challenge());

        onlineGameMap.put(challengeEvent.id(), onlineGame);

        client.challengeAccept(challengeEvent.id());
    }

    private void declineChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Challenge is not acceptable, declining...");
        client.challengeDecline(challengeEvent.id());
    }

    private void startGame(Event.GameStartEvent gameStartEvent) {
        logger.info("Game to start: {}", gameStartEvent);

        if (!onlineGameMap.containsKey(gameStartEvent.id())) {
            logger.info("Game {} is not in memory, resigning", gameStartEvent.id());
            client.gameResign(gameStartEvent.id());
            return;
        }

        var onlineGame = onlineGameMap.get(gameStartEvent.id());

        onlineGame.start(gameStartEvent);
    }

    private void gameFinish(Event.GameStopEvent gameStopEvent) {
        if (!onlineGameMap.containsKey(gameStopEvent.id())) {
            logger.info("Game {} finished but not in memory", gameStopEvent.id());
            return;
        }

        var onlineGame = onlineGameMap.get(gameStopEvent.id());

        logger.info("Game {} finished, cleaning memory", gameStopEvent.id());

        onlineGameMap.remove(gameStopEvent.id());

        onlineGame.stop(gameStopEvent);
    }

}
