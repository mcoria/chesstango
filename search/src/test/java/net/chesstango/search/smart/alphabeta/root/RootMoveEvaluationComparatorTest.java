package net.chesstango.search.smart.alphabeta.root;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Unit tests for RootMoveEvaluation class, focusing on its compareTo method.
 * The compareTo method first compares evaluations, and if they are equal, compares bounds.
 */
public class RootMoveEvaluationComparatorTest {

    RootMoveEvaluationCollection.RootMoveEvaluationComparator rootMoveEvaluationComparator;

    @BeforeEach
    public void setup() {
        rootMoveEvaluationComparator = new RootMoveEvaluationCollection.RootMoveEvaluationComparator();
    }

    /**
     * Ambos RootMoveEvaluation coinciden en BOUND pero difieren en EVALUATION.
     */
    @Test
    public void testCompareTo_EvaluationsDifferent_BoundEquals() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, -100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        /**
         * Cuando ordenamos para Blancas: los valores mayores preceden a los menores
         */
        assertTrue(rootMoveEvaluationComparator.compare(evaluation1, evaluation2) > 0);
        assertTrue(rootMoveEvaluationComparator.compare(evaluation2, evaluation1) < 0);
    }

    @Test
    public void testCompareTo_EvaluationsEquals_BoundsDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 100, Bound.LOWER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 100, Bound.EXACT);

        assertTrue(rootMoveEvaluationComparator.compare(evaluation1, evaluation2) < 0);
        assertTrue(rootMoveEvaluationComparator.compare(evaluation2, evaluation1) > 0);
    }

    /**
     * Resuelve por movimiento
     */
    @Test
    public void testCompareTo_EvaluationsEquals_BoundsEquals() {
        Move move = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move, 100, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move, 100, Bound.EXACT);

        assertEquals(0, rootMoveEvaluationComparator.compare(evaluation1, evaluation2));
        assertEquals(0, rootMoveEvaluationComparator.compare(evaluation2, evaluation1));
    }

    @Test
    public void testCompareTo_EvaluationsEquals_BoundDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 150, Bound.UPPER_BOUND);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 150, Bound.LOWER_BOUND);

        assertTrue(rootMoveEvaluationComparator.compare(evaluation1, evaluation2) > 0);
        assertTrue(rootMoveEvaluationComparator.compare(evaluation2, evaluation1) < 0);
    }

    @Test
    public void testCompareTo_EvaluationsDifferent_BoundDifferent() {
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);

        RootMoveEvaluation evaluation1 = new RootMoveEvaluation(move1, 200, Bound.EXACT);
        RootMoveEvaluation evaluation2 = new RootMoveEvaluation(move2, 200, Bound.UPPER_BOUND);

        assertTrue(rootMoveEvaluationComparator.compare(evaluation1, evaluation2) < 0);
        assertTrue(rootMoveEvaluationComparator.compare(evaluation2, evaluation1) > 0);
    }

    private Move createSimpleKnightMove(PiecePositioned from, PiecePositioned to) {
        return createMove(from, to);
    }
    private Move createMove(PiecePositioned from, PiecePositioned to) {
        return new Move() {
            @Override
            public PiecePositioned getFrom() {
                return from;
            }

            @Override
            public PiecePositioned getTo() {
                return to;
            }

            @Override
            public void executeMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public void undoMove() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public Cardinal getMoveDirection() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public boolean isQuiet() {
                throw new RuntimeException("Not meant for execution");
            }

            @Override
            public long getZobristHash() {
                throw new RuntimeException("Not meant for execution");
            }
        };
    }
}