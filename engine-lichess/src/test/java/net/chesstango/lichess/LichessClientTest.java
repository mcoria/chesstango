package net.chesstango.lichess;

import chariot.Client;
import chariot.ClientAuth;
import chariot.api.BotApiAuth;
import chariot.model.GameStateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

/**
 * @author Mauricio Coria
 */
public class LichessClientTest {
    private static final URI lichessApi = URI.create("https://lichess.org");
    private static final String BOT_TOKEN = "TOKEN";

    private BotApiAuth bot;

    @BeforeEach
    public void setup() {
        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), BOT_TOKEN);
        bot = clientAuth.bot();
    }

    // https://lichess.org/LoFfzmT9
    @Test
    @Disabled
    public void test_TerminatedGame() {
        try (Stream<GameStateEvent> gameEvents = bot.connectToGame("LoFfzmT9").stream()) {
            gameEvents.forEach(System.out::println);
            System.out.println("gameEvents consumed");
        } catch (RuntimeException e) {
            throw e;
        }
    }

    // https://lichess.org/cMS4Qc3K
    @Test
    @Disabled
    public void test_GameAborted() {
        try (Stream<GameStateEvent> gameEvents = bot.connectToGame("cMS4Qc3K").stream()) {
            gameEvents.forEach(System.out::println);
            System.out.println("gameEvents consumed");
        } catch (RuntimeException e) {
            throw e;
        }
    }

    // https://lichess.org/dXLNmiwT
    @Test
    @Disabled
    public void test_GameStarted() {
        AtomicBoolean abortedGame = new AtomicBoolean(false);
        try (ExecutorService executorService = Executors.newSingleThreadExecutor();
             Stream<GameStateEvent> gameEvents = bot.connectToGame("dXLNmiwT").stream()) {
            gameEvents.forEach(event -> {
                System.out.println(event);
                if (abortedGame.get()) return;
                executorService.submit(() -> {
                    if (!abortedGame.get()) {
                        System.out.println("Claiming victory");
                        bot.abort("dXLNmiwT");
                        abortedGame.set(true);
                    }
                });
            });
            System.out.println("gameEvents consumed");
        } catch (RuntimeException e) {
            throw e;
        }
    }
}
