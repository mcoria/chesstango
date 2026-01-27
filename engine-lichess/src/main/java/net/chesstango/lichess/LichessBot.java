package net.chesstango.lichess;

import chariot.model.Event;
import lombok.extern.slf4j.Slf4j;
import net.chesstango.engine.Tango;

import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
@Slf4j
public class LichessBot implements Runnable {

    private final Tango tango;

    private final LichessClient client;

    public LichessBot(LichessClient client, Tango tango) {
        this.client = client;
        this.tango = tango;
    }

    @Override
    public void run() {
        log.info("Connection successful, entering main event loop...");

        try (Stream<Event> events = client.streamEvents()) {
            events.forEach(event -> {
                log.info("event received: {}", event);
                switch (event.type()) {
                    case challenge:
                        log.info("Accepting challenge {}", event.id());
                        client.challengeAccept(event.id());
                        break;
                    case challengeCanceled:
                        log.info("ChallengeCanceledEvent: {}", event.id());
                        break;
                    case challengeDeclined:
                        log.info("ChallengeDeclinedEvent: {}", event.id());
                        break;
                    case gameStart:
                        gameStart((Event.GameStartEvent) event);
                        break;
                    case gameFinish:
                        log.info("GameStopEvent {}", event.id());
                        break;
                }
            });
            log.info("main event loop finished");
        } catch (RuntimeException e) {
            log.error("main event loop failed", e);
            System.exit(-1);
        }
    }


    private synchronized void gameStart(Event.GameStartEvent gameStartEvent) {
        log.info("GameStartEvent {}", gameStartEvent.id());

        try {
            LichessGame onlineGame = new LichessGame(client, gameStartEvent, tango.newSession());

            onlineGame.run();

        } catch (RuntimeException e) {
            log.error("Error executing onlineGame", e);
            System.exit(-1);
        }
    }

}
