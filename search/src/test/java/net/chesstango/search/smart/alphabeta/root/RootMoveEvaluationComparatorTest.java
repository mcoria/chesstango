package net.chesstango.search.smart.alphabeta.root;

import net.chesstango.board.Color;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for RootMoveEvaluation class, focusing on its compareTo method.
 * The compareTo method first compares evaluations, and if they are equal, compares bounds.
 */
public class RootMoveEvaluationComparatorTest {

    RootMoveEvaluationCollection.RootMoveEvaluationComparator whiteRootMoveEvaluationComparator;
    RootMoveEvaluationCollection.RootMoveEvaluationComparator blackRootMoveEvaluationComparator;

    @BeforeEach
    public void setup() {
        whiteRootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator(Color.WHITE);
        blackRootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator(Color.BLACK);
    }

    /**
     * Ambos RootMoveEvaluation coinciden en BOUND pero difieren en EVALUATION.
     */
    @Test
    public void testCompareTo_BothEvaluationsDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, -100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        /**
         * Cuando ordenamos para Blancas: los valores mayores preceden a los menores
         */
        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation1, evaluation2) > 0);
        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation2, evaluation1) < 0);

        /**
         * Cuando ordenamos para Negra: los valores menores preceden a los mayores
         */
        assertTrue(blackRootMoveEvaluationComparator.compare(evaluation1, evaluation2) < 0);
        assertTrue(blackRootMoveEvaluationComparator.compare(evaluation2, evaluation1) > 0);
    }

    @Test
    public void testCompareTo_EvaluationsSame_BoundDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.LOWER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation1, evaluation2) < 0);
        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation2, evaluation1) > 0);
    }

    @Test
    @Disabled
    public void testCompareTo_EvaluationsAndBoundsSame() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        assertEquals(0, whiteRootMoveEvaluationComparator.compare(evaluation1, evaluation2));
        assertEquals(0, whiteRootMoveEvaluationComparator.compare(evaluation2, evaluation1));
    }

    @Test
    public void testCompareTo_BoundComparison() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 150, Bound.UPPER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 150, Bound.LOWER_BOUND);

        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation1, evaluation2) > 0);
        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation2, evaluation1) < 0);
    }

    @Test
    public void testCompareTo_BoundComparisonWithExact() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 200, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 200, Bound.UPPER_BOUND);

        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation1, evaluation2) < 0);
        assertTrue(whiteRootMoveEvaluationComparator.compare(evaluation2, evaluation1) > 0);
    }
}