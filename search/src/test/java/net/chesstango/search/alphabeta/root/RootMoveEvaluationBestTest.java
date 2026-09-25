package net.chesstango.search.alphabeta.root;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.search.Bound.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationBestTest {

    private RootMoveEvaluationBest rootMoveEvaluationBest;

    @BeforeEach
    public void setup() {
        rootMoveEvaluationBest = new RootMoveEvaluationBest();
        rootMoveEvaluationBest.beforeSearchByDepth();
    }

    @Test
    public void test01() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, LOWER_BOUND, null));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move, maxEvaluation.move());
        assertEquals(0, maxEvaluation.evaluation());
        assertEquals(LOWER_BOUND, maxEvaluation.bound());
    }

    @Test
    public void test02() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, UPPER_BOUND, null));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertNull(maxEvaluation);
    }


    @Test
    public void test03() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, EXACT, null));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move, maxEvaluation.move());
        assertEquals(0, maxEvaluation.evaluation());
        assertEquals(EXACT, maxEvaluation.bound());
    }


    @Test
    public void test04() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, -1000, EXACT, null));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 0, EXACT, null));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(0, maxEvaluation.evaluation());
        assertEquals(EXACT, maxEvaluation.bound());
    }


    @Test
    public void test05() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, 1000, EXACT, null));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 1000, LOWER_BOUND, null));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(Bound.LOWER_BOUND, maxEvaluation.bound());
    }

    @Test
    public void test06() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, 1000, EXACT, null));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 1000, UPPER_BOUND, null));

        RootMoveEvaluation minEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move1, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(EXACT, minEvaluation.bound());
    }

}
