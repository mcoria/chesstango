package net.chesstango.board.representations.fen;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FENParserTest {

    private FENParser fenParser;

    @BeforeEach
    public void setUp() throws Exception {
        fenParser = new FENParser();
    }

    @Test
    public void testParseFEN() {
        String fenString = "rnbqkb1r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKB1R w KQkq - 0 1";

        FEN fen = fenParser.parseFEN(fenString);

        assertEquals("rnbqkb1r/pppppppp/8/8/8/8/PPPPPPPP/RNBQKB1R", fen.getPiecePlacement());
        assertEquals("w", fen.getActiveColor());
        assertEquals("KQkq", fen.getCastingsAllowed());
        assertEquals("0", fen.getHalfMoveClock());
        assertEquals("1", fen.getFullMoveClock());
        assertEquals(fenString, fen.toString());
    }
}

