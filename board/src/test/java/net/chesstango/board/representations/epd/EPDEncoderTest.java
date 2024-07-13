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
        epd.setFen(FEN.of(FENDecoder.INITIAL_FEN));
        epd.setSuppliedMoveStr("a4");
        epd.setId("1");

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 sm a4; id \"1\";", epdEncoder.encode(epd));
    }
}