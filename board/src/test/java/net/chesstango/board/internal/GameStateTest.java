package net.chesstango.board.internal;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class GameStateTest {

    @Test
    public void testGetPreviousGameState01() {
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);

        Move move = game.getMove(Square.e2, Square.e4);
        move.executeMove();

        assertNotNull(game.getHistory().peekLastRecord());
        assertEquals(move, game.getHistory().peekLastRecord().playedMove());

        move.undoMove();

        assertNull(game.getHistory().peekLastRecord());
    }

    @Test
    public void testGetPreviousGameState02() {
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);

        Move move1 = game.getMove(Square.e2, Square.e4);
        move1.executeMove();
        assertNotNull(game.getHistory().peekLastRecord());
        assertEquals(move1, game.getHistory().peekLastRecord().playedMove());

        Move move2 = game.getMove(Square.e7, Square.e6);
        move2.executeMove();
        assertNotNull(game.getHistory().peekLastRecord());
        assertEquals(move2, game.getHistory().peekLastRecord().playedMove());

        move2.undoMove();
        assertNotNull(game.getHistory().peekLastRecord());
        assertEquals(move1, game.getHistory().peekLastRecord().playedMove());


        move1.undoMove();
        assertNull(game.getHistory().peekLastRecord());
    }

}
