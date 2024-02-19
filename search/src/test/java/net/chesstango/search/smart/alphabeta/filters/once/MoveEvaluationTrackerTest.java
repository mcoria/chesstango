package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveFactory;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTrackerTest {
    private MoveEvaluationTracker moveEvaluationTracker;

    private MoveFactory moveFactory;

    @BeforeEach
    public void setup() {
        moveEvaluationTracker = new MoveEvaluationTracker();
        moveFactory = SingletonMoveFactories.getDefaultMoveFactoryWhite();

        moveEvaluationTracker.beforeSearch(new SearchByCycleContext(null));
        moveEvaluationTracker.beforeSearchByDepth(new SearchByDepthContext(1));
    }

    @Test
    public void test01() {
        final Move move1 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.EXACT));

        final Move move2 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 2000, MoveEvaluationType.EXACT));

        final Move move3 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.c2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move3, 3000, MoveEvaluationType.EXACT));


        MoveEvaluation maxEvaluation = moveEvaluationTracker.getBestMoveEvaluation(true);
        assertNotNull(maxEvaluation);
        assertEquals(move3, maxEvaluation.move());
        assertEquals(3000, maxEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, maxEvaluation.moveEvaluationType());


        MoveEvaluation minEvaluation = moveEvaluationTracker.getBestMoveEvaluation(false);
        assertNotNull(minEvaluation);
        assertEquals(move1, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, minEvaluation.moveEvaluationType());
    }


    @Test
    public void test02() {
        final Move move1 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.LOWER_BOUND));

        final Move move2 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 1000, MoveEvaluationType.EXACT));

        final Move move3 = moveFactory.createSimpleMove(PiecePositioned.getPiecePositioned(Square.c2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move3, 1000, MoveEvaluationType.UPPER_BOUND));


        MoveEvaluation maxEvaluation = moveEvaluationTracker.getBestMoveEvaluation(true);
        assertNotNull(maxEvaluation);
        assertEquals(move1, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(MoveEvaluationType.LOWER_BOUND, maxEvaluation.moveEvaluationType());


        MoveEvaluation minEvaluation = moveEvaluationTracker.getBestMoveEvaluation(false);
        assertNotNull(minEvaluation);
        assertEquals(move3, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(MoveEvaluationType.UPPER_BOUND, minEvaluation.moveEvaluationType());
    }

}
