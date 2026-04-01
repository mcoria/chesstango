package net.chesstango.search;

import net.chesstango.board.moves.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for RootMoveEvaluation class, focusing on its compareTo method.
 * The compareTo method first compares evaluations, and if they are equal, compares bounds.
 */
class RootMoveEvaluationTest {

    @Test
    void testCompareTo_BothEvaluationsDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 200, Bound.EXACT);

        assertTrue(evaluation1.compareTo(evaluation2) < 0);
        assertTrue(evaluation2.compareTo(evaluation1) > 0);
    }

    @Test
    void testCompareTo_EvaluationsSame_BoundDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.LOWER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        assertTrue(evaluation1.compareTo(evaluation2) > 0);
        assertTrue(evaluation2.compareTo(evaluation1) < 0);
    }

    @Test
    void testCompareTo_EvaluationsAndBoundsSame() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        assertEquals(0, evaluation1.compareTo(evaluation2));
        assertEquals(0, evaluation2.compareTo(evaluation1));
    }

    @Test
    void testCompareTo_BoundComparison() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 150, Bound.UPPER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 150, Bound.LOWER_BOUND);

        assertTrue(evaluation1.compareTo(evaluation2) < 0);
        assertTrue(evaluation2.compareTo(evaluation1) > 0);
    }

    @Test
    void testCompareTo_BoundComparisonWithExact() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 200, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 200, Bound.UPPER_BOUND);

        assertTrue(evaluation1.compareTo(evaluation2) > 0);
        assertTrue(evaluation2.compareTo(evaluation1) < 0);
    }
}