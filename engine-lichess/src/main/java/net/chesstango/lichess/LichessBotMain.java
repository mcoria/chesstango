package net.chesstango.lichess;

import chariot.Client;
import chariot.ClientAuth;
import net.chesstango.engine.Tango;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class LichessBotMain {
    private static final Logger logger = LoggerFactory.getLogger(LichessBotMain.class);

    public static final String POLYGLOT_BOOK = "POLYGLOT_BOOK"; //Key para leer properties
    private static final URI lichessApi = URI.create("https://lichess.org");

    private static String BOT_TOKEN;
    private static boolean CHALLENGE_BOTS;
    private static final Map<String, Object> PROPERTIES = new HashMap<>();


    public static void main(String[] args) {

        readEnvironmentVars();

        ClientAuth clientAuth = Client.auth(conf -> conf.api(lichessApi), BOT_TOKEN);

        LichessClient lichessClient = new LichessClient(clientAuth);

        if (clientAuth.scopes().stream().anyMatch(Client.Scope.bot_play::equals)) {
            logger.info("Start playing as a bot");

            Tango tango = new Tango();

            tango.open();

            String polyglotBookPath = (String) PROPERTIES.get(LichessBotMain.POLYGLOT_BOOK);
            if (Objects.nonNull(polyglotBookPath)) {
                tango.setPolyglotBook(polyglotBookPath);
            }

            new LichessBot(lichessClient, CHALLENGE_BOTS, tango)
                    .run();

            tango.close();

        } else {
            throw new RuntimeException("BOT_TOKEN is missing scope bot:play");
        }
    }

    private static void readEnvironmentVars() {
        BOT_TOKEN = System.getenv("BOT_TOKEN");
        if (Objects.isNull(BOT_TOKEN) || BOT_TOKEN.isEmpty()) {
            throw new RuntimeException("BOT_TOKEN is missing");
        }

        String challengeBots = System.getenv("CHALLENGE_BOTS");
        if (challengeBots != null && !challengeBots.isEmpty()) {
            CHALLENGE_BOTS = Boolean.parseBoolean(challengeBots);
        }

        String polyglotBookPath = System.getenv(POLYGLOT_BOOK);
        if (Objects.nonNull(polyglotBookPath)) {
            PROPERTIES.put(POLYGLOT_BOOK, polyglotBookPath);
        }
    }

}
