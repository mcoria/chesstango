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
        GameState gameState = game.getState();

        Move move = game.getMove(Square.e2, Square.e4);
        game.executeMove(move);

        assertNotNull(gameState.getPreviosGameState());
        assertEquals(move, gameState.getPreviosGameState().selectedMove);

        game.undoMove();

        assertNull(gameState.getPreviosGameState());
    }

    @Test
    public void testGetPreviosGameState02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        GameState gameState = game.getState();

        Move move1 = game.getMove(Square.e2, Square.e4);
        game.executeMove(move1);
        assertNotNull(gameState.getPreviosGameState());
        assertEquals(move1, gameState.getPreviosGameState().selectedMove);

        Move move2 = game.getMove(Square.e7, Square.e6);
        game.executeMove(move2);
        assertNotNull(gameState.getPreviosGameState());
        assertEquals(move2, gameState.getPreviosGameState().selectedMove);

        game.undoMove();
        assertNotNull(gameState.getPreviosGameState());
        assertEquals(move1, gameState.getPreviosGameState().selectedMove);


        game.undoMove();
        assertNull(gameState.getPreviosGameState());
    }

}
