package net.chesstango.lichess;

import chariot.Client;
import chariot.ClientAuth;
import net.chesstango.engine.Config;
import net.chesstango.engine.Tango;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class LichessBotMain {

    private static final Logger logger = LoggerFactory.getLogger(LichessBotMain.class);

    private static String BOT_TOKEN;

    private static boolean CHALLENGE_BOTS;

    private static final String POLYGLOT_BOOK_FILE = "POLYGLOT_BOOK_FILE";
    private static final String SYZYGY_DIRECTORY = "SYZYGY_DIRECTORY";

    private static final Map<String, Object> PROPERTIES = new HashMap<>();


    public static void main(String[] args) {

        readEnvironmentVars();

        ClientAuth clientAuth = Client.auth(BOT_TOKEN);

        LichessClient lichessClient = new LichessClient(clientAuth);

        if (clientAuth.scopes().stream().anyMatch(Client.Scope.bot_play::equals)) {
            logger.info("Start playing as a bot");

            Config config = new Config();

            String polyglotFile = (String) PROPERTIES.get(LichessBotMain.POLYGLOT_BOOK_FILE);
            if (Objects.nonNull(polyglotFile)) {
                config.setPolyglotFile(polyglotFile);
            }

            String syzygyDirectory = (String) PROPERTIES.get(LichessBotMain.SYZYGY_DIRECTORY);
            if (Objects.nonNull(polyglotFile)) {
                config.setSyzygyDirectory(syzygyDirectory);
            }

            try (Tango tango = Tango.open(config)) {
                new LichessBot(lichessClient, CHALLENGE_BOTS, tango)
                        .run();
            }

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

        String polyglotBookPath = System.getenv(POLYGLOT_BOOK_FILE);
        if (Objects.nonNull(polyglotBookPath)) {
            PROPERTIES.put(POLYGLOT_BOOK_FILE, polyglotBookPath);
        }
    }

}
