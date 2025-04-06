package net.chesstango.board.internal;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 *
 */
public class GameStateTest {

    @Test
    public void testGetPreviousGameState01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move move = game.getMove(Square.e2, Square.e4);
        move.executeMove();

        assertNotNull(game.getPreviousState());
        assertEquals(move, game.getPreviousState().getSelectedMove());

        move.undoMove();

        assertNull(game.getPreviousState());
    }

    @Test
    public void testGetPreviousGameState02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move move1 = game.getMove(Square.e2, Square.e4);
        move1.executeMove();
        assertNotNull(game.getPreviousState());
        assertEquals(move1, game.getPreviousState().getSelectedMove());

        Move move2 = game.getMove(Square.e7, Square.e6);
        move2.executeMove();
        assertNotNull(game.getPreviousState());
        assertEquals(move2, game.getPreviousState().getSelectedMove());

        move2.undoMove();
        assertNotNull(game.getPreviousState());
        assertEquals(move1, game.getPreviousState().getSelectedMove());


        move1.undoMove();
        assertNull(game.getPreviousState());
    }

}
