package net.chesstango.li;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.ChallengeInfo;
import chariot.model.Event;
import chariot.model.Unlimited;
import chariot.model.VariantType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessMainService implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(LichessMainService.class);
    private static final int MAX_SIMULTANEOUS_GAMES = 2;

    public static void main(String[] args) {
        URI lichessApi = URI.create("https://lichess.org");

        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), "lip_Qk9igzwz1rgEWOUOKf6P");

        logger.info("Start playing as a bot");

        new LichessMainService(new LichessClient(clientAuth)).run();
    }

    private final ScheduledExecutorService gameExecutorService;

    private final Map<String, LichessGame> onlineGameMap = new HashMap<>();

    private final LichessClient client;

    public LichessMainService(LichessClient client) {
        this.client = client;
        this.gameExecutorService = Executors.newScheduledThreadPool(MAX_SIMULTANEOUS_GAMES);
    }

    @Override
    public void run() {
        Stream<Event> events = client.streamEvents();

        logger.info("Connection successful, entering main event loop...");

        events.forEach(event -> {
            logger.info("event received: {}", event);
            switch (event.type()) {
                case challenge -> newChallenge((Event.ChallengeEvent) event);
                case challengeCanceled, challengeDeclined ->
                        logger.info("Challenge cancelled / declined: {}", event.id());
                case gameStart -> startGame((Event.GameStartEvent) event);
                case gameFinish -> gameFinish((Event.GameStopEvent) event);
            }
        });

        logger.info("main event loop finished");
    }

    private void newChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("New challenge received: {}", challengeEvent.challenge());

        if (isChallengeAcceptable(challengeEvent)) {
            acceptChallenge(challengeEvent);
        } else {
            declineChallenge(challengeEvent);
        }
    }

    private boolean isChallengeAcceptable(Event.ChallengeEvent challengeEvent) {
        ChallengeInfo challenge = challengeEvent.challenge();

        return VariantType.Variant.standard.equals(challenge.gameType().variant()) && challenge.gameType().timeControl() instanceof Unlimited && onlineGameMap.size() < MAX_SIMULTANEOUS_GAMES;
    }

    private void acceptChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Accepting challenge {}", challengeEvent.id());

        var onlineGame = new LichessGame(client, challengeEvent.id());

        onlineGame.setChallenge(challengeEvent.challenge());

        onlineGameMap.put(challengeEvent.id(), onlineGame);

        client.challengeAccept(challengeEvent.id());
    }

    private void declineChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Challenge is not acceptable, declining...");
        client.challengeDecline(challengeEvent.id());
    }

    private void startGame(Event.GameStartEvent gameStartEvent) {
        logger.info("Starting game {}", gameStartEvent.id());

        if (!onlineGameMap.containsKey(gameStartEvent.id())) {
            logger.info("Game {} is not in memory, resigning", gameStartEvent.id());
            client.gameResign(gameStartEvent.id());
            return;
        }

        var onlineGame = onlineGameMap.get(gameStartEvent.id());

        onlineGame.setGame(gameStartEvent.game());

        gameExecutorService.submit(onlineGame);
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
