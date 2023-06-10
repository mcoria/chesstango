package net.chesstango.board;

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
    public void testGetPreviosGameState01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move move = game.getMove(Square.e2, Square.e4);
        game.executeMove(move);

        assertNotNull(game.getState().getPreviousState());
        assertEquals(move, game.getState().getPreviousState().getSelectedMove());

        game.undoMove();

        assertNull(game.getState().getPreviousState());
    }

    @Test
    public void testGetPreviosGameState02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        GameStateReader gameState = game.getState();

        Move move1 = game.getMove(Square.e2, Square.e4);
        game.executeMove(move1);
        assertNotNull(game.getState().getPreviousState());
        assertEquals(move1, game.getState().getPreviousState().getSelectedMove());

        Move move2 = game.getMove(Square.e7, Square.e6);
        game.executeMove(move2);
        assertNotNull(game.getState().getPreviousState());
        assertEquals(move2, game.getState().getPreviousState().getSelectedMove());

        game.undoMove();
        assertNotNull(game.getState().getPreviousState());
        assertEquals(move1, game.getState().getPreviousState().getSelectedMove());


        game.undoMove();
        assertNull(game.getState().getPreviousState());
    }

}
