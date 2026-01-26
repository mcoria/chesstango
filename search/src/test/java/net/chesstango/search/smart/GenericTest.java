package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Search;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Mauricio Coria
 */
public abstract class GenericTest {

    protected Search search;

    @Test
    public void testHorizonteEffectCapture() {
        Game game = Game.from(FEN.of("3q3k/3r4/8/3p4/8/8/3R4/3Q3K w - - 0 1"));

        Move bestMove = search.startSearch(game).getBestMove();

        Move rookCapturePawn = game.getMove(Square.d2, Square.d5);

        assertNotEquals(rookCapturePawn, bestMove.getFrom());
    }

    @Test
    public void testHorizonteEffectPromotion() {
        Game game = Game.from(FEN.of("6k1/8/8/8/3Q4/2n5/3p3K/8 w - - 2 1"));

        Move bestMove = search.startSearch(game).getBestMove();

        Move queenCaptureKnight = game.getMove(Square.d4, Square.c3);

        assertNotEquals(queenCaptureKnight, bestMove);
    }

    @Test
    public void testDeterministicMove() {
        Game game = Game.from(FEN.START_POSITION);

        Move bestMove = search.startSearch(game).getBestMove();

        Game gameMirror = game.mirror();

        Move bestMoveMirror = search.startSearch(gameMirror).getBestMove();

        assertEquals(bestMove.getFrom().piece().getOpposite(), bestMoveMirror.getFrom().piece());

        assertEquals(bestMove.getFrom().square().mirror(), bestMoveMirror.getFrom().square());

        assertEquals(bestMove.getTo().square().mirror(), bestMoveMirror.getTo().square());
    }

}
