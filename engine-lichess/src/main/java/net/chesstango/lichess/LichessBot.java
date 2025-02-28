package net.chesstango.lichess;

import chariot.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.io.UncheckedIOException;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessBot implements Runnable, LichessBotMBean {
    private static final Logger logger = LoggerFactory.getLogger(LichessBot.class);

    private final boolean challengeRandomBot;

    private final Map<String, Object> properties;

    private final ScheduledExecutorService gameExecutorService;

    private final LichessClient client;

    private final int maxSimultaneousGames;

    private final AtomicInteger gameCounter = new AtomicInteger(0);

    private final LichessChallenger lichessChallenger;

    private final LichessChallengeHandler lichessChallengeHandler;

    private ScheduledFuture<?> challengeRandomBotScheduler;


    public LichessBot(LichessClient client, int maxSimultaneousGames, boolean challengeRandomBot, Map<String, Object> properties) {
        this.client = client;
        this.maxSimultaneousGames = maxSimultaneousGames;
        this.challengeRandomBot = challengeRandomBot;
        this.properties = properties;
        this.gameExecutorService = Executors.newScheduledThreadPool(maxSimultaneousGames);
        this.lichessChallengeHandler = new LichessChallengeHandler(client, maxSimultaneousGames, gameCounter);
        this.lichessChallenger = new LichessChallenger(client);
    }

    @Override
    public void run() {
        try {
            registerMBean();

            Stream<Event> events = client.streamEvents();

            logger.info("Connection successful, entering main event loop...");

            if (challengeRandomBot) {
                challengeRandomBotScheduler = gameExecutorService.scheduleWithFixedDelay(this::challengeRandomBot, 30, 10, TimeUnit.SECONDS);
            }

            events.forEach(event -> {
                logger.info("event received: {}", event);
                switch (event.type()) {
                    case challenge -> lichessChallengeHandler.newChallenge((Event.ChallengeEvent) event);
                    case challengeCanceled, challengeDeclined ->
                            logger.info("Challenge cancelled / declined: {}", event.id());
                    case gameStart -> gameStart((Event.GameStartEvent) event);
                    case gameFinish -> gameFinish((Event.GameStopEvent) event);
                }
            });

            logger.info("main event loop finished");
        } catch (UncheckedIOException uio) {
            logger.error("UncheckedIOException", uio);
        }
    }

    @Override
    public void challengeUser(String user, String type) {
        ChallengeType challengeType = switch (type) {
            case "BULLET" -> ChallengeType.BULLET;
            case "BLITZ" -> ChallengeType.BLITZ;
            case "RAPID" -> ChallengeType.RAPID;
            default -> null;
        };

        if (challengeType == null) {
            logger.error("unknown challengeType type, valid values: BULLET BLITZ RAPID");
            return;
        }

        // Si no se alcanzo la cantidad de juegos maximos
        if (gameCounter.get() < maxSimultaneousGames) {
            logger.info("Challenging User {} {}", user, type);

            lichessChallenger.challengeUser(user, challengeType);
        } else {
            logger.info("Scheduler Busy");
        }
    }

    @Override
    public void challengeRandomBot() {
        // Si no se alcanzo la cantidad de juegos maximos
        if (gameCounter.get() < maxSimultaneousGames) {
            logger.info("Challenging random bot");
            lichessChallenger.challengeRandomBot();
        } else {
            logger.info("Scheduler Busy");
        }
    }

    @Override
    public void stopAcceptingChallenges() {
        logger.info("stopAcceptingChallenges() invoked");
        if (challengeRandomBotScheduler != null && !challengeRandomBotScheduler.isCancelled()) {
            challengeRandomBotScheduler.cancel(false);
        }
        lichessChallengeHandler.stopAcceptingChallenges();
    }


    private void gameStart(Event.GameStartEvent gameStartEvent) {
        logger.info("GameStartEvent {}", gameStartEvent.id());
        gameExecutorService.submit(() -> {
            try {
                gameCounter.incrementAndGet();
                LichessTango onlineGame = new LichessTango(client, gameStartEvent.id(), properties);
                onlineGame.setGameInfo(gameStartEvent.game());
                onlineGame.run();
            } finally {
                gameCounter.decrementAndGet();
            }
        });
    }

    private void gameFinish(Event.GameStopEvent gameStopEvent) {
        logger.info("GameStopEvent {}", gameStopEvent.id());
    }

    private void registerMBean() {
        try {

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName("net.chesstango.li:type=LichessBotMain,name=chesstango_bot");

            mbs.registerMBean(this, name);

        } catch (Exception e) {
            logger.error("Error registering MBean", e);
            throw new RuntimeException(e);
        }
    }

}
