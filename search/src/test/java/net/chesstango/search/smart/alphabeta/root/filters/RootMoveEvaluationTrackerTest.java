package net.chesstango.search.smart.alphabeta.root.filters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class RootMoveEvaluationTrackerTest {
    private RootMoveEvaluationTracker moveEvaluationTracker;

    @Mock
    private RootMoveEvaluationCollection moveEvaluations;

    @BeforeEach
    public void setup() {
        moveEvaluationTracker = new RootMoveEvaluationTracker();
        moveEvaluationTracker.setRootMoveEvaluationCollection(moveEvaluations);
    }



    /**
     * Este caso es interesante, representa una busqueda con windows muy cerrado.
     * Hay SOLO dos movimientos; el primero por debajo de alpha y el segundo por arriba de beta.
     * Ninguno es valido
     */
    @Test
    public void test_process01() {
        AlphaBetaFunction fn = mock(AlphaBetaFunction.class);
        when(fn.search(0, -500, 500))
                .thenReturn(-1000);
        when(moveEvaluations.get(any(Move.class))).thenReturn(Optional.empty());

        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);

        Move move = game.getMove(Square.a2, Square.a3);
        move.executeMove();
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        verify(moveEvaluations, times(1)).get(move);
        verify(moveEvaluations, times(1)).save(any(RootMoveEvaluation.class));
    }

    @Test
    public void test_process02() {
        AlphaBetaFunction fn = mock(AlphaBetaFunction.class);
        when(fn.search(0, -500, 500))
                .thenReturn(1000);
        when(moveEvaluations.get(any(Move.class))).thenReturn(Optional.empty());

        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);

        Move move = game.getMove(Square.b2, Square.b3);
        move.executeMove();
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        verify(moveEvaluations, times(1)).get(move);
        verify(moveEvaluations, times(1)).save(any(RootMoveEvaluation.class));
    }

    @Test
    public void test_createRootMoveEvaluationExactBound() {
        Move move = mock(Move.class);
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation(move, 0, -10, 10);

        assertEquals(move, result.move());
        assertEquals(0, result.evaluation());
        assertEquals(Bound.EXACT, result.bound());
    }

    @Test
    public void test_createRootMoveEvaluationUpperBound() {
        Move move = mock(Move.class);
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation(move, -20, -10, 10);

        assertEquals(move, result.move());
        assertEquals(-20, result.evaluation());
        assertEquals(Bound.UPPER_BOUND, result.bound());
    }

    @Test
    public void test_createRootMoveEvaluationLowerBound() {
        Move move = mock(Move.class);
        RootMoveEvaluation result = moveEvaluationTracker.createRootMoveEvaluation(move, 20, -10, 10);

        assertEquals(move, result.move());
        assertEquals(20, result.evaluation());
        assertEquals(Bound.LOWER_BOUND, result.bound());
    }


}
