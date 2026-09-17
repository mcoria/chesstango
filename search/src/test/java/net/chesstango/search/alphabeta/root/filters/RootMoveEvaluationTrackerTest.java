package net.chesstango.search.alphabeta.root.filters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.alphabeta.pv.model.PVCalculator;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class RootMoveEvaluationTrackerTest {
    private RootMoveEvaluationTracker moveEvaluationTracker;

    @Mock
    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    @Mock
    private RootMoveEvaluationBest rootMoveEvaluationBest;

    @Mock
    private PVCalculator pvCalculator;

    private Move move;

    @BeforeEach
    public void setup() {
        moveEvaluationTracker = new RootMoveEvaluationTracker();
        moveEvaluationTracker.setRootMoveEvaluationBest(rootMoveEvaluationBest);
        moveEvaluationTracker.setRootMoveEvaluationCollection(rootMoveEvaluationCollection);
        moveEvaluationTracker.setPvCalculator(pvCalculator);

        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);

        move = game.getMove(Square.a2, Square.a3);
        move.executeMove();
    }


    /**
     * Este caso es interesante, representa una busqueda con windows muy cerrado.
     * Hay SOLO dos movimientos; el primero por debajo de alpha y el segundo por arriba de beta.
     * Ninguno es valido
     */
    @Test
    public void test_process01() {
        AlphaBeta fn = mock(AlphaBeta.class);
        when(fn.alphaBeta(0, -500, 500))
                .thenReturn(-1000);

        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);
        moveEvaluationTracker.setNext(fn);

        Move move = game.getMove(Square.a2, Square.a3);
        move.executeMove();
        moveEvaluationTracker.alphaBeta(0, -500, 500);
        game.undoMove();

        verify(rootMoveEvaluationCollection, times(1)).save(any(RootMoveEvaluation.class));
        verify(rootMoveEvaluationBest, times(1)).save(any(RootMoveEvaluation.class));
    }

    @Test
    public void test_process02() {
        AlphaBeta fn = mock(AlphaBeta.class);
        when(fn.alphaBeta(0, -500, 500))
                .thenReturn(1000);

        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);
        moveEvaluationTracker.setNext(fn);

        Move move = game.getMove(Square.b2, Square.b3);
        move.executeMove();
        moveEvaluationTracker.alphaBeta(0, -500, 500);
        game.undoMove();

        verify(rootMoveEvaluationCollection, times(1)).save(any(RootMoveEvaluation.class));
        verify(rootMoveEvaluationBest, times(1)).save(any(RootMoveEvaluation.class));
    }

    @Test
    public void test_createRootMoveEvaluationExactBound() {
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation( 0, -10, 10);

        assertEquals(move, result.move());
        assertEquals(0, result.evaluation());
        assertEquals(Bound.EXACT, result.bound());
        verify(pvCalculator, times(1)).calculatePrincipalVariation(0);
    }

    @Test
    public void test_createRootMoveEvaluationUpperBound() {
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation( -20, -10, 10);

        assertEquals(move, result.move());
        assertEquals(-20, result.evaluation());
        assertEquals(Bound.UPPER_BOUND, result.bound());
        assertNotNull(result.pv());
    }

    @Test
    public void test_createRootMoveEvaluationLowerBound() {
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation( 20, -10, 10);

        assertEquals(move, result.move());
        assertEquals(20, result.evaluation());
        assertEquals(Bound.LOWER_BOUND, result.bound());
        assertNotNull(result.pv());
    }


}
