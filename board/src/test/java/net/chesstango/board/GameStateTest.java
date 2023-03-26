package net.chesstango.board;

import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Test;

import static org.junit.Assert.*;

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

        assertNotNull(game.getState().getPreviosState());
        assertEquals(move, game.getState().getPreviosState().getSelectedMove());

        game.undoMove();

        assertNull(game.getState().getPreviosState());
    }

    @Test
    public void testGetPreviosGameState02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        GameStateReader gameState = game.getState();

        Move move1 = game.getMove(Square.e2, Square.e4);
        game.executeMove(move1);
        assertNotNull(game.getState().getPreviosState());
        assertEquals(move1, game.getState().getPreviosState().getSelectedMove());

        Move move2 = game.getMove(Square.e7, Square.e6);
        game.executeMove(move2);
        assertNotNull(game.getState().getPreviosState());
        assertEquals(move2, game.getState().getPreviosState().getSelectedMove());

        game.undoMove();
        assertNotNull(game.getState().getPreviosState());
        assertEquals(move1, game.getState().getPreviosState().getSelectedMove());


        game.undoMove();
        assertNull(game.getState().getPreviosState());
    }

}
