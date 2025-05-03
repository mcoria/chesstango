package net.chesstango.board.representations.fen;

import net.chesstango.board.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 *
 */
public class FENBuilderWithoutClocksTest {

    private FENEncoderWithoutClocks coder ;

    @BeforeEach
    public void setUp() throws Exception {
        coder = new FENEncoderWithoutClocks();
    }


    @Test
    public void test_encode_without_clocks(){
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);

        game.getPosition().constructChessPositionRepresentation(coder);

        String fenWithoutClocks = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -", fenWithoutClocks);
    }
}
