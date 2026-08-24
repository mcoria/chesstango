package net.chesstango.search.smart.core.filters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.smart.AlphaBetaFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class QuiescenceStandingPatTest {

    @Mock
    private AlphaBetaFilter next;

    @Mock
    private Evaluator evaluator;

    @Mock
    private Move mockMove;

    private QuiescenceStandingPat quiescenceStandingPat;

    private Move[] bestMoves;

    private Game game;

    @BeforeEach
    public void setup() {
        game = Game.from(FEN.START_POSITION);
        bestMoves = new Move[40];
        Arrays.fill(bestMoves, mockMove);

        quiescenceStandingPat = new QuiescenceStandingPat();
        quiescenceStandingPat.setNext(next);
        quiescenceStandingPat.setEvaluator(evaluator);
        quiescenceStandingPat.setBestMoves(bestMoves);
        quiescenceStandingPat.setGame(game);
    }

    /**
     * Test when the standing pat value exceeds or equals beta.
     * This ensures the search is pruned early.
     */
    @Test
    void testStandingPatExceedsBeta() {
        // Setup
        when(evaluator.evaluate()).thenReturn(20);

        int alpha = 5;
        int beta = 15;
        int currentPly = 0;

        // Execute
        int result = quiescenceStandingPat.alphaBeta(currentPly, alpha, beta);

        // Verify
        assertEquals(20, result);
        assertEquals(null, bestMoves[currentPly]);
        verify(next, never()).alphaBeta(anyInt(), anyInt(), anyInt());
    }

    /**
     * Test when the next quiescenceStandingPat returns a value smaller than standing pat.
     * The method should return the standing pat value and clear the moves.
     */
    @Test
    void testStandingPatGreaterThanNextFilter() {
        // Setup
        when(evaluator.evaluate()).thenReturn(10);
        when(next.alphaBeta(0, 10, 15)).thenAnswer( _ ->{
            bestMoves[0] = game.getMove(Square.a2, Square.a3);
            return 8;
        });

        int alpha = 5;
        int beta = 15;
        int currentPly = 0;

        // Execute
        int result = quiescenceStandingPat.alphaBeta(currentPly, alpha, beta);

        // Verify
        assertEquals(10, result);
        assertEquals(null, bestMoves[currentPly]);
        verify(next).alphaBeta(0, 10, 15);
    }

    /**
     * Test when the next quiescenceStandingPat returns a value greater than or equal to standing pat.
     * The method should return the next quiescenceStandingPat's value.
     */
    @Test
    void testNextFilterGreaterThanStandingPat() {
        // Setup
        when(evaluator.evaluate()).thenReturn(10);
        when(next.alphaBeta(0, 10, 15)).thenAnswer( _ ->{
            bestMoves[0] = game.getMove(Square.a2, Square.a3);
            return 12;
        });

        int alpha = 5;
        int beta = 15;
        int currentPly = 0;

        // Execute
        int result = quiescenceStandingPat.alphaBeta(currentPly, alpha, beta);

        // Verify
        assertEquals(12, result);
        assertEquals(game.getMove(Square.a2, Square.a3), bestMoves[currentPly]);
        verify(next).alphaBeta(0, 10, 15);
    }

    /**
     * Test when alpha is greater than standing pat.
     * The method should use alpha instead of standing pat when calling next filter.
     */
    @Test
    void testAlphaGreaterThanStandingPat() {
        // Setup
        when(evaluator.evaluate()).thenReturn(10);
        when(next.alphaBeta(0, 12, 15)).thenAnswer(_ -> {
            bestMoves[0] = game.getMove(Square.a2, Square.a3);
            return 13;
        });

        int alpha = 12;
        int beta = 15;
        int currentPly = 0;

        // Execute
        int result = quiescenceStandingPat.alphaBeta(currentPly, alpha, beta);

        // Verify
        assertEquals(13, result);
        assertEquals(game.getMove(Square.a2, Square.a3), bestMoves[currentPly]);
        verify(next).alphaBeta(0, 12, 15);
    }

    /**
     * Test when neither standing pat nor next filter returns a value greater than alpha.
     * The method should return the next filter's value when it's greater than standing pat.
     */
    @Test
    void testStandingPatAndNextFilterNotGreaterThanAlpha() {
        // Setup
        when(evaluator.evaluate()).thenReturn(5);
        when(next.alphaBeta(0, 15, 20)).thenAnswer(_ -> {
            bestMoves[0] = game.getMove(Square.a2, Square.a3);
            return 8;
        });

        int alpha = 15;
        int beta = 20;
        int currentPly = 0;

        // Execute
        int result = quiescenceStandingPat.alphaBeta(currentPly, alpha, beta);

        // Verify
        assertEquals(8, result);
        assertEquals(game.getMove(Square.a2, Square.a3), bestMoves[currentPly]);
        verify(next).alphaBeta(0, 15, 20);
    }


}
