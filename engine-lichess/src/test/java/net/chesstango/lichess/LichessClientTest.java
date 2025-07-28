package net.chesstango.lichess;

import chariot.Client;
import chariot.ClientAuth;
import chariot.api.BotApiAuth;
import chariot.model.GameStateEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URI;
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
}
