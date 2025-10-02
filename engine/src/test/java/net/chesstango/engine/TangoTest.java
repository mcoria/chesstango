package net.chesstango.engine;

import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class TangoTest {

    private Config config;

    @BeforeEach
    public void setup() {
        config = new Config();
    }

    @Test
    @Disabled
    public void testOpenTango() {
        config.setPolyglotFile("C:/java/projects/chess/chess-utils/books/openings/polyglot-collection/komodo.bin");
        //config.setSyzygyDirectory("C:/java/projects/chess/chess-utils/books/syzygy/3-4-5");
        //config.setSyncSearch(true);

        try (Tango tango = Tango.open(config);) {
            tango.newSession(FEN.of(FENParser.INITIAL_FEN));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
