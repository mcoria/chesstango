package net.chesstango.board.representations.epd;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class EPDEncoderTest {

    private EPDEncoder epdEncoder;

    @BeforeEach
    public void setup() {
        epdEncoder = new EPDEncoder();
    }

    @Test
    public void testEncode() {
        EPD epd = new EPD();
        epd.setFenWithoutClocks(FEN.of(FENDecoder.INITIAL_FEN));
        epd.setSuppliedMoveStr("a4");
        epd.setId("1");
        epd.setC0("c0");
        epd.setC1("c1");
        epd.setC2("c2");
        epd.setC3("c3");
        epd.setC4("c4");
        epd.setC5("c5");
        epd.setC6("c6");

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - sm a4; c0 \"c0\"; c1 \"c1\"; c2 \"c2\"; c3 \"c3\"; c4 \"c4\"; c5 \"c5\"; c6 \"c6\"; id \"1\";", epdEncoder.encode(epd));
    }
}