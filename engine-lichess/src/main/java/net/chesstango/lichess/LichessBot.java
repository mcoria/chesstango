package net.chesstango.lichess;

import chariot.model.Event;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.Tango;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessBot implements Runnable, LichessBotMBean {

    private final Tango tango;

    private final LichessClient client;

    private final LichessChallenger lichessChallenger;

    private final LichessChallengeHandler lichessChallengeHandler;

    private final ScheduledExecutorService timerExecutor;

    public LichessBot(LichessClient client, Tango tango) {
        this.client = client;
        this.tango = tango;
        this.timerExecutor = Executors.newSingleThreadScheduledExecutor(new LichessBotThreadFactory("timer"));
        this.lichessChallengeHandler = new LichessChallengeHandler(client, () -> false);
        this.lichessChallenger = new LichessChallenger(client);
    }

    @Override
    public void run() {
        registerMBean();

        log.info("Connection successful, entering main event loop...");

        try (Stream<Event> events = client.streamEvents()) {
            events.forEach(event -> {
                log.info("event received: {}", event);
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
            log.info("main event loop finished");
        } catch (RuntimeException e) {
            log.error("main event loop failed", e);
        } finally {
            stopAcceptingChallenges();
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
            log.error("unknown challengeType type, valid values: BULLET BLITZ RAPID");
            return;
        }

        lichessChallenger.challengeUser(user, challengeType);
    }

    @Override
    public void challengeRandomBot() {
        try {
            lichessChallenger.challengeRandomBot();
        } catch (RuntimeException e) {
            log.error("challengeRandomBot failed", e);
            System.exit(-1);
        }
    }

    @Override
    public void stopAcceptingChallenges() {
        log.info("stopAcceptingChallenges() invoked");
        lichessChallengeHandler.stopAcceptingChallenges();
    }

    private synchronized void gameStart(Event.GameStartEvent gameStartEvent) {
        log.info("GameStartEvent {}", gameStartEvent.id());

        try {
            LichessGame onlineGame = new LichessGame(client, gameStartEvent, tango);

            log.info("Scheduling gameWatchDog");
            ScheduledFuture<?> gameWatchDog = timerExecutor.scheduleWithFixedDelay(onlineGame::gameWatchDog, 120, 60, TimeUnit.SECONDS);

            onlineGame.run();

            log.info("Cancelling gameWatchDog");
            gameWatchDog.cancel(false);

        } catch (RuntimeException e) {
            log.error("Error executing onlineGame", e);
            System.exit(-1);
        }
    }

    private void gameStop(Event.GameStopEvent gameStopEvent) {
        log.info("GameStopEvent {}", gameStopEvent.id());
    }

    private void registerMBean() {
        try {

            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

            ObjectName name = new ObjectName("net.chesstango.li:type=LichessBotMain,name=chesstango_bot");

            mbs.registerMBean(this, name);

        } catch (Exception e) {
            log.error("Error registering MBean", e);
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
