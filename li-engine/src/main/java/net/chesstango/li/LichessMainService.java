package net.chesstango.li;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessMainService implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(LichessMainService.class);
    private static final int MAX_SIMULTANEOUS_GAMES = 2;

    public static void main(String[] args) {
        URI lichessApi = URI.create("https://lichess.org");

        String token = System.getenv("BOT_TOKEN");

        if (Objects.nonNull(token) && !token.isEmpty()) {
            ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), token);
            if (clientAuth.scopes().contains(Client.Scope.bot_play)) {
                logger.info("Start playing as a bot");
                new LichessMainService(new LichessClient(clientAuth)).run();
            } else {
                throw new RuntimeException("BOT_TOKEN is missing scope bot:play");
            }
        } else {
            throw new RuntimeException("BOT_TOKEN is missing");
        }
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
        GameType gameType = challengeEvent.challenge().gameType();

        return isVariantAcceptable(gameType.variant())                    // Chess variant
                && isTimeControlAcceptable(gameType.timeControl())        // Time control
                && onlineGameMap.size() < MAX_SIMULTANEOUS_GAMES;         // Not busy..
    }

    private static boolean isVariantAcceptable(VariantType variant) {
        return VariantType.Variant.standard.equals(variant) || variant instanceof VariantType.FromPosition;
    }

    private static boolean isTimeControlAcceptable(TimeControl timeControl) {
        Predicate<RealTime> supportedRealtimeGames = realtime ->
                (Enums.Speed.blitz.equals(realtime.speed()) || Enums.Speed.rapid.equals(realtime.speed()))
                        && realtime.initial().getSeconds() >= 30L;

        // timeControl instanceof Unlimited                     // Unlimited games are not supported for the moment
        return (timeControl instanceof RealTime realtime        // Realtime
                && supportedRealtimeGames.test(realtime));
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

        onlineGame.start(gameStartEvent);

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
