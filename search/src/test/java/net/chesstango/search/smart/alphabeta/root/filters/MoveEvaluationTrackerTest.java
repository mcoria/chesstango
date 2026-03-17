package net.chesstango.search.smart.alphabeta.root.filters;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.RootChildEvaluation;
import net.chesstango.search.Bound;
import net.chesstango.search.smart.alphabeta.AlphaBetaFunction;
import net.chesstango.search.smart.alphabeta.AlphaBetaHelper;
import net.chesstango.search.smart.alphabeta.root.RootChildEvaluationCollection;
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
    private RootChildEvaluationTracker moveEvaluationTracker;
    private RootChildEvaluationCollection moveEvaluations;

    @BeforeEach
    public void setup() {
        moveEvaluationTracker = new RootChildEvaluationTracker();
        moveEvaluations = new RootChildEvaluationCollection();
        moveEvaluationTracker.setMoveEvaluations(moveEvaluations);
    }

    @Test
    public void test01() {
        moveEvaluations.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        moveEvaluations.add(new RootChildEvaluation(move1, 1000, Bound.EXACT));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        moveEvaluations.add(new RootChildEvaluation(move2, 2000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        moveEvaluations.add(new RootChildEvaluation(move3, 3000, Bound.EXACT));


        Optional<RootChildEvaluation> maxEvaluationOpt = moveEvaluations.getBestMoveEvaluation(true);
        assertTrue(maxEvaluationOpt.isPresent());
        RootChildEvaluation maxEvaluation = maxEvaluationOpt.get();
        assertEquals(move3, maxEvaluation.move());
        assertEquals(3000, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.moveEvaluationType());


        Optional<RootChildEvaluation> minEvaluationOpt = moveEvaluations.getBestMoveEvaluation(false);
        assertTrue(minEvaluationOpt.isPresent());
        RootChildEvaluation minEvaluation = minEvaluationOpt.get();
        assertEquals(move1, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(Bound.EXACT, minEvaluation.moveEvaluationType());
    }


    @Test
    public void test02() {
        moveEvaluations.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        moveEvaluations.add(new RootChildEvaluation(move1, 1000, Bound.LOWER_BOUND));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        moveEvaluations.add(new RootChildEvaluation(move2, 1000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        moveEvaluations.add(new RootChildEvaluation(move3, 1000, Bound.UPPER_BOUND));


        Optional<RootChildEvaluation> maxEvaluationOpt = moveEvaluations.getBestMoveEvaluation(true);
        assertTrue(maxEvaluationOpt.isPresent());
        RootChildEvaluation maxEvaluation = maxEvaluationOpt.get();
        assertEquals(move2, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.moveEvaluationType());


        Optional<RootChildEvaluation> minEvaluationOpt = moveEvaluations.getBestMoveEvaluation(false);
        assertTrue(minEvaluationOpt.isPresent());
        RootChildEvaluation minEvaluation = minEvaluationOpt.get();
        assertEquals(move2, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(Bound.EXACT, minEvaluation.moveEvaluationType());
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
                .thenReturn(AlphaBetaHelper.encode(-1000))
                .thenReturn(AlphaBetaHelper.encode(1000));


        Game game = Game.from(FEN.START_POSITION);
        moveEvaluationTracker.setGame(game);
        moveEvaluations.beforeSearchByDepth();

        game.executeMove(Square.a2, Square.a3);
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        game.executeMove(Square.b2, Square.b3);
        moveEvaluationTracker.process(0, -500, 500, fn);
        game.undoMove();

        Optional<RootChildEvaluation> maxEvaluationOpt = moveEvaluations.getBestMoveEvaluation(true);
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
