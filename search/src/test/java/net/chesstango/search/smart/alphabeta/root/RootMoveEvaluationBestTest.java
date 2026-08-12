package net.chesstango.search.smart.alphabeta.root;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    }

    @Test
    public void test01() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, Bound.LOWER_BOUND));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertNull(maxEvaluation);
    }

    @Test
    public void test02() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, Bound.UPPER_BOUND));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertNull(maxEvaluation);
    }


    @Test
    public void test03() {
        final Move move = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move, 0, Bound.EXACT));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move, maxEvaluation.move());
        assertEquals(0, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.bound());
    }


    @Test
    public void test04() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, -1000, Bound.EXACT));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 0, Bound.EXACT));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(0, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.bound());
    }


    /**
     * Esta sequencia no debiera darse, igual la pruebo
     */
    @Test
    public void test05() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, 1000, Bound.LOWER_BOUND));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 1000, Bound.EXACT));

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.bound());
    }

    @Test
    public void test06() {
        final Move move1 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move1, 1000, Bound.UPPER_BOUND));

        final Move move2 = mock(Move.class);
        rootMoveEvaluationBest.save(new RootMoveEvaluation(move2, 1000, Bound.EXACT));

        RootMoveEvaluation minEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();
        assertEquals(move2, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(Bound.EXACT, minEvaluation.bound());
    }

}
