package net.chesstango.board.representations.fen;

import net.chesstango.board.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 *
 */
public class FENEncoderZobristTest {
    private FENEncoderZobrist coder ;


    @BeforeEach
    public void setUp() throws Exception {
        coder = new FENEncoderZobrist();
    }


    @Test
    public void test_encode_zobrist(){
        Game game = FENDecoder.loadGame("5rk1/1ppb3p/p1pb4/6q1/1P1r4/2PQR2P/P2B2P1/6KN b - b3");

        game.getPosition().constructChessPositionRepresentation(coder);

        String fenZobrist = coder.getChessRepresentation();

        assertEquals("5rk1/1ppb3p/p1pb4/6q1/1P1r4/2PQR2P/P2B2P1/6KN b - -", fenZobrist);
    }
}
