package net.chesstango.li;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessBotMain implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(LichessBotMain.class);
    private static String BOT_TOKEN;
    private static Integer MAX_SIMULTANEOUS_GAMES;
    private static String POLYGLOT_BOOK;

    private static Map<String, Object> properties = new HashMap<>();

    public static void main(String[] args) {
        URI lichessApi = URI.create("https://lichess.org");

        getEnvs();

        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), BOT_TOKEN);
        if (clientAuth.scopes().contains(Client.Scope.bot_play)) {
            logger.info("Start playing as a bot");
            new LichessBotMain(new LichessClient(clientAuth)).run();
        } else {
            throw new RuntimeException("BOT_TOKEN is missing scope bot:play");
        }
    }

    private static void getEnvs() {
        BOT_TOKEN = System.getenv("BOT_TOKEN");
        if (Objects.isNull(BOT_TOKEN) || BOT_TOKEN.isEmpty()) {
            throw new RuntimeException("BOT_TOKEN is missing");
        }

        MAX_SIMULTANEOUS_GAMES = Integer.parseInt(System.getenv("MAX_SIMULTANEOUS_GAMES"));
        if (Objects.isNull(MAX_SIMULTANEOUS_GAMES)) {
            throw new RuntimeException("MAX_SIMULTANEOUS_GAMES is missing");
        } else if (MAX_SIMULTANEOUS_GAMES <= 0) {
            throw new RuntimeException("MAX_SIMULTANEOUS_GAMES value is wrong");
        }

        properties.put("MAX_SIMULTANEOUS_GAMES", MAX_SIMULTANEOUS_GAMES);

        POLYGLOT_BOOK = System.getenv("POLYGLOT_BOOK");
        if(Objects.nonNull(POLYGLOT_BOOK)){
            properties.put("POLYGLOT_BOOK", POLYGLOT_BOOK);
        }
    }

    private final ScheduledExecutorService gameExecutorService;

    private final Map<String, LichessTango> onlineGameMap = Collections.synchronizedMap(new HashMap<>());

    private final LichessClient client;


    public LichessBotMain(LichessClient client) {
        this.client = client;
        this.gameExecutorService = Executors.newScheduledThreadPool(MAX_SIMULTANEOUS_GAMES + 1);
    }

    @Override
    public void run() {
        Stream<Event> events = client.streamEvents();

        logger.info("Connection successful, entering main event loop...");

        //gameExecutorService.scheduleWithFixedDelay(this::challengeRandomBot, 10, 30, TimeUnit.SECONDS);

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
        logger.info("New challenge received: {}", challengeEvent.id());

        if (isChallengeAcceptable(challengeEvent)) {
            acceptChallenge(challengeEvent);
        } else {
            declineChallenge(challengeEvent);
        }
    }

    private void acceptChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Accepting challenge {}", challengeEvent.id());

        client.challengeAccept(challengeEvent.id());
    }

    private void declineChallenge(Event.ChallengeEvent challengeEvent) {
        logger.info("Challenge is not acceptable, declining... {}", challengeEvent.id());
        client.challengeDecline(challengeEvent.id());
    }

    private void startGame(Event.GameStartEvent gameStartEvent) {
        logger.info("Starting game {}", gameStartEvent.id());

        LichessTango onlineGame = new LichessTango(client, gameStartEvent.id(), properties);

        onlineGameMap.put(gameStartEvent.id(), onlineGame);

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

    private void challengeRandomBot() {
        try {
            long timeControlledGames = onlineGameMap.values()
                    .stream()
                    .filter(LichessTango::isTimeControlledGame)
                    .count();
            if (timeControlledGames == 0) {
                logger.info("Challenging random bot");
                client.challengeRandomBot();
            } else {
                logger.info("Engine is playing");
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }


    private boolean isChallengeAcceptable(Event.ChallengeEvent challengeEvent) {
        GameType gameType = challengeEvent.challenge().gameType();

        long timeControlledGames = onlineGameMap.values().stream().filter(LichessTango::isTimeControlledGame).count();

        return isVariantAcceptable(gameType.variant())                    // Chess variant
                && isTimeControlAcceptable(gameType.timeControl())        // Time control
                && timeControlledGames < MAX_SIMULTANEOUS_GAMES;         // Not busy..
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
