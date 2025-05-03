package net.chesstango.search.smart.alphabeta.filters.once;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.MoveEvaluationType;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFunction;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTrackerTest {
    private MoveEvaluationTracker moveEvaluationTracker;

    @BeforeEach
    public void setup() {
        moveEvaluationTracker = new MoveEvaluationTracker();
    }

    @Test
    public void test01() {
        moveEvaluationTracker.beforeSearch(new SearchByCycleContext(null));
        moveEvaluationTracker.beforeSearchByDepth(new SearchByDepthContext(1));

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.EXACT));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 2000, MoveEvaluationType.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
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

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move1, 1000, MoveEvaluationType.LOWER_BOUND));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        moveEvaluationTracker.trackMoveEvaluation(new MoveEvaluation(move2, 1000, MoveEvaluationType.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
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
     */
    @Test
    public void test03() {
        AlphaBetaFunction fn = mock(AlphaBetaFunction.class);
        when(fn.search(0, -500, 500))
                .thenReturn(TranspositionEntry.encode(-1000))
                .thenReturn(TranspositionEntry.encode(1000));


        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);
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
