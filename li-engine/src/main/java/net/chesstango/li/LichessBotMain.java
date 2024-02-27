package net.chesstango.li;

import chariot.Client;
import chariot.ClientAuth;
import chariot.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessBotMain implements Runnable, LichessBotMainMBean {
    private static final Logger logger = LoggerFactory.getLogger(LichessBotMain.class);
    private static final URI lichessApi = URI.create("https://lichess.org");
    private static final Map<String, Object> properties = new HashMap<>();
    public static final String POLYGLOT_BOOK = "POLYGLOT_BOOK"; //Key para leer properties
    private static String BOT_TOKEN;
    private static Integer MAX_SIMULTANEOUS_GAMES;
    private static boolean CHALLENGE_BOTS;

    public static void main(String[] args) {

        readEnvironmentVars();

        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), BOT_TOKEN);

        if (clientAuth.scopes().contains(Client.Scope.bot_play)) {
            logger.info("Start playing as a bot");
            new LichessBotMain(new LichessClient(clientAuth))
                    .run();
        } else {
            throw new RuntimeException("BOT_TOKEN is missing scope bot:play");
        }
    }

    private static void readEnvironmentVars() {
        BOT_TOKEN = System.getenv("BOT_TOKEN");
        if (Objects.isNull(BOT_TOKEN) || BOT_TOKEN.isEmpty()) {
            throw new RuntimeException("BOT_TOKEN is missing");
        }

        MAX_SIMULTANEOUS_GAMES = Integer.parseInt(System.getenv("MAX_SIMULTANEOUS_GAMES"));
        if (MAX_SIMULTANEOUS_GAMES <= 0) {
            throw new RuntimeException("MAX_SIMULTANEOUS_GAMES value is wrong");
        }

        String challengeBots = System.getenv("CHALLENGE_BOTS");
        if (challengeBots != null && !challengeBots.isEmpty()) {
            CHALLENGE_BOTS = Boolean.parseBoolean(challengeBots);
        }

        String polyglotBookPath = System.getenv(POLYGLOT_BOOK);
        if (Objects.nonNull(polyglotBookPath)) {
            properties.put(POLYGLOT_BOOK, polyglotBookPath);
        }
    }

    private final ScheduledExecutorService gameExecutorService;

    private final Map<String, LichessTango> onlineGameMap = Collections.synchronizedMap(new HashMap<>());

    private final LichessClient client;

    private final LichessChallenger lichessChallenger;

    private final LichessChangeHandler lichessChangeHandler;

    private ScheduledFuture<?> challengeRandomBotFuture;


    public LichessBotMain(LichessClient client) {
        this.client = client;
        this.gameExecutorService = Executors.newScheduledThreadPool(MAX_SIMULTANEOUS_GAMES + 1);
        this.lichessChangeHandler = new LichessChangeHandler(client, MAX_SIMULTANEOUS_GAMES, onlineGameMap);
        this.lichessChallenger = new LichessChallenger(client);
    }

    @Override
    public void run() {
        registerMBean();
        do {
            try {
                Stream<Event> events = client.streamEvents();

                logger.info("Connection successful, entering main event loop...");

                if (CHALLENGE_BOTS) {
                    challengeRandomBotFuture = gameExecutorService.scheduleWithFixedDelay(this::challengeRandomBot, 30, 60, TimeUnit.SECONDS);
                }

                events.forEach(event -> {
                    logger.info("event received: {}", event);
                    switch (event.type()) {
                        case challenge -> lichessChangeHandler.newChallenge((Event.ChallengeEvent) event);
                        case challengeCanceled, challengeDeclined ->
                                logger.info("Challenge cancelled / declined: {}", event.id());
                        case gameStart -> gameStart((Event.GameStartEvent) event);
                        case gameFinish -> gameFinish((Event.GameStopEvent) event);
                    }
                });

                logger.info("main event loop finished");
            } catch (UncheckedIOException uio) {
                logger.error("UncheckedIOException", uio);
                try {
                    logger.info("Trying to reconnect");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (true);
    }

    @Override
    public void challengeUser(String user, String type) {
        logger.info("challengeUser({}}, {}) invoked", user, type);
        LichessChallenger.ChallengeType challengeType = switch (type) {
            case "BULLET" -> LichessChallenger.ChallengeType.BULLET;
            case "BLITZ" -> LichessChallenger.ChallengeType.BLITZ;
            case "RAPID" -> LichessChallenger.ChallengeType.RAPID;
            default -> null;
        };

        if (challengeType == null) {
            logger.error("unknown challengeType type, valid values: BULLET BLITZ RAPID");
            return;
        }

        lichessChallenger.challengeUser(user, challengeType);
    }

    @Override
    public synchronized void stopChallengingBots() {
        logger.info("stopChallengeBot() invoked");
        if (challengeRandomBotFuture != null && !challengeRandomBotFuture.isCancelled()) {
            challengeRandomBotFuture.cancel(false);
        }
        lichessChangeHandler.stopAcceptingChallenges();
    }


    private void gameStart(Event.GameStartEvent gameStartEvent) {
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
            // Contar la cantidad de juegos activos
            long timeControlledGames = onlineGameMap
                    .values()
                    .stream()
                    .filter(LichessTango::isTimeControlledGame)
                    .count();

            // Si no hay juego activo, buscar contrincante
            if (timeControlledGames == 0) {
                logger.info("Challenging random bot");
                lichessChallenger.challengeRandomBot();
            }
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
        }
    }

    private void registerMBean() {
        try {

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName("net.chesstango.li:type=LichessBotMain,name=chesstango_bot");

            mbs.registerMBean(this, name);

        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException(e);
        }
    }

}
