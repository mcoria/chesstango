package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    }

    @Test
    public void test01() {
        moveEvaluationTracker.beforeSearch(new SearchByCycleContext(null));
        moveEvaluationTracker.beforeSearchByDepth(new SearchByDepthContext(1));

        final Move move1 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.EXACT));

        final Move move2 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 2000, MoveEvaluationType.EXACT));

        final Move move3 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.c2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move3, 3000, MoveEvaluationType.EXACT));


        Optional<MoveEvaluation> maxEvaluationOpt = moveEvaluationTracker.getBestMoveEvaluation(true);
        assertTrue(maxEvaluationOpt.isPresent());
        MoveEvaluation maxEvaluation = maxEvaluationOpt.get();
        assertEquals(move3, maxEvaluation.move());
        assertEquals(3000, maxEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, maxEvaluation.moveEvaluationType());


        Optional<MoveEvaluation> minEvaluationOpt = moveEvaluationTracker.getBestMoveEvaluation(false);
        assertTrue(minEvaluationOpt.isPresent());
        MoveEvaluation minEvaluation = minEvaluationOpt.get();
        assertEquals(move1, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, minEvaluation.moveEvaluationType());
    }


    @Test
    public void test02() {
        moveEvaluationTracker.beforeSearch(new SearchByCycleContext(null));
        moveEvaluationTracker.beforeSearchByDepth(new SearchByDepthContext(1));

        final Move move1 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.LOWER_BOUND));

        final Move move2 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 1000, MoveEvaluationType.EXACT));

        final Move move3 = moveFactory.createSimpleKnightMove(PiecePositioned.getPiecePositioned(Square.c2, Piece.PAWN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move3, 1000, MoveEvaluationType.UPPER_BOUND));


        Optional<MoveEvaluation> maxEvaluationOpt = moveEvaluationTracker.getBestMoveEvaluation(true);
        assertTrue(maxEvaluationOpt.isPresent());
        MoveEvaluation maxEvaluation = maxEvaluationOpt.get();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, maxEvaluation.moveEvaluationType());


        Optional<MoveEvaluation> minEvaluationOpt = moveEvaluationTracker.getBestMoveEvaluation(false);
        assertTrue(minEvaluationOpt.isPresent());
        MoveEvaluation minEvaluation = minEvaluationOpt.get();
        assertEquals(move2, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(MoveEvaluationType.EXACT, minEvaluation.moveEvaluationType());
    }


    /**
     * Este caso es interesante, representa una busqueda con windows muy cerrado.
     * Hay SOLO dos movimientos; el primero por debajo de alpha y el segundo por arriba de beta.
     * Ninguno es valido
     *
     */
    @Test
    public void test03() {
        AlphaBetaFunction fn = mock(AlphaBetaFunction.class);
        when(fn.search(0, -500, 500))
                .thenReturn(TranspositionEntry.encode(-1000))
                .thenReturn(TranspositionEntry.encode(1000));


        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        moveEvaluationTracker.beforeSearch(new SearchByCycleContext(game));
        moveEvaluationTracker.beforeSearchByDepth(new SearchByDepthContext(1));

        game.executeMove(Square.a2, Square.a3);
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        game.executeMove(Square.b2, Square.b3);
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        Optional<MoveEvaluation> maxEvaluationOpt = moveEvaluationTracker.getBestMoveEvaluation(true);
        assertTrue(maxEvaluationOpt.isEmpty());
    }

}
