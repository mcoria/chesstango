package net.chesstango.lichess;

import chariot.model.Event;
import net.chesstango.engine.Tango;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessBot implements Runnable, LichessBotMBean {
    private static final Logger logger = LoggerFactory.getLogger(LichessBot.class);

    private final boolean challengeRandomBot;

    private final Tango tango;

    private final LichessClient client;

    private final LichessChallenger lichessChallenger;

    private final LichessChallengeHandler lichessChallengeHandler;

    private final ExecutorService gameExecutor;

    private final ScheduledExecutorService challengeRandomBotExecutor;

    private ScheduledFuture<?> challengeRandomBotTask;

    private Future<?> job;


    public LichessBot(LichessClient client, boolean challengeRandomBot, Tango tango) {
        this.client = client;
        this.challengeRandomBot = challengeRandomBot;
        this.tango = tango;
        this.challengeRandomBotExecutor = Executors.newScheduledThreadPool(1, new LichessBotThreadFactory("challenger"));
        this.gameExecutor = Executors.newScheduledThreadPool(1, new LichessBotThreadFactory("game"));
        this.lichessChallengeHandler = new LichessChallengeHandler(client, this::isBusy);
        this.lichessChallenger = new LichessChallenger(client);
    }

    @Override
    public void run() {
        registerMBean();

        logger.info("Connection successful, entering main event loop...");

        if (challengeRandomBot) {
            challengeRandomBotTask = challengeRandomBotExecutor.scheduleWithFixedDelay(this::challengeRandomBot, 60, 30, TimeUnit.SECONDS);
        }

        try (Stream<Event> events = client.streamEvents()) {
            events.forEach(event -> {
                logger.info("event received: {}", event);
                switch (event.type()) {
                    case challenge -> lichessChallengeHandler.challengeCreated((Event.ChallengeCreatedEvent) event);
                    case challengeCanceled ->
                            lichessChallengeHandler.challengeCanceled((Event.ChallengeCanceledEvent) event);
                    case challengeDeclined ->
                            lichessChallengeHandler.challengeDeclined((Event.ChallengeDeclinedEvent) event);
                    case gameStart -> gameStart((Event.GameStartEvent) event);
                    case gameFinish -> gameStop((Event.GameStopEvent) event);
                }
            });
            logger.info("main event loop finished");
        } catch (RuntimeException e) {
            logger.error("main event loop failed", e);
        } finally {
            stopAcceptingChallenges();
            challengeRandomBotExecutor.close();
            gameExecutor.close();
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
        if (!isBusy()) {
            logger.info("Challenging User {} {}", user, type);
            lichessChallenger.challengeUser(user, challengeType);
        } else {
            logger.info("challengeUser: Scheduler Busy");
        }
    }

    @Override
    public void challengeRandomBot() {
        // Si no se alcanzo la cantidad de juegos maximos
        if (!isBusy()) {
            logger.info("Challenging random bot");
            lichessChallenger.challengeRandomBot();
        } else {
            logger.info("challengeRandomBot: Scheduler Busy");
        }
    }

    @Override
    public void stopAcceptingChallenges() {
        logger.info("stopAcceptingChallenges() invoked");
        lichessChallengeHandler.stopAcceptingChallenges();
        if (challengeRandomBotTask != null && !challengeRandomBotTask.isCancelled()) {
            challengeRandomBotTask.cancel(false);
        }
    }

    private synchronized void gameStart(Event.GameStartEvent gameStartEvent) {
        logger.info("GameStartEvent {}", gameStartEvent.id());
        if (!isBusy()) {
            job = gameExecutor.submit(() -> {
                if (challengeRandomBot) {
                    challengeRandomBotTask.cancel(false);
                }

                LichessGame onlineGame = new LichessGame(client, gameStartEvent.id(), tango);
                onlineGame.setGameInfo(gameStartEvent.game());
                onlineGame.run();

                if (challengeRandomBot) {
                    challengeRandomBotTask = challengeRandomBotExecutor.scheduleWithFixedDelay(this::challengeRandomBot, 60, 30, TimeUnit.SECONDS);
                }
            });
        } else {
            logger.info("[{}] GameExecutor is busy, aborting game", gameStartEvent.id());
            client.gameAbort(gameStartEvent.id());
        }
    }

    private synchronized boolean isBusy() {
        return job != null && !job.isDone();
    }

    private void gameStop(Event.GameStopEvent gameStopEvent) {
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

    public static class LichessBotThreadFactory implements ThreadFactory {
        private final AtomicInteger threadCounter = new AtomicInteger(1);
        private String threadNamePrefix = "";

        public LichessBotThreadFactory(String threadNamePrefix) {
            this.threadNamePrefix = threadNamePrefix;
        }

        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, String.format("%s-%d", threadNamePrefix, threadCounter.getAndIncrement()));
        }
    }

}
