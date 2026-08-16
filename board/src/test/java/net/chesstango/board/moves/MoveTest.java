package net.chesstango.board.moves;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.gardel.fen.FEN;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 *
 */
public class MoveTest {

    @Test
    public void testEquals() {
        Game game1 = Game.from(FEN.START_POSITION);
        Game game2 = Game.from(FEN.START_POSITION);

        Move move1 = game1.getMove(Square.e2, Square.e4);
        Move move2 = game2.getMove(Square.e2, Square.e4);

        assertEquals(move1, move2);
        assertEquals(move2, move1);
    }
}
