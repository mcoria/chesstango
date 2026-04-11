package net.chesstango.search.smart.alphabeta.root;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.RootMoveEvaluation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * @author Mauricio Coria
 */
public class RootMoveEvaluationCollectionTest {

    private RootMoveEvaluationCollection rootMoveEvaluationCollection;

    @BeforeEach
    public void setup() {
        rootMoveEvaluationCollection = new RootMoveEvaluationCollection();
    }

    @Test
    public void test01() {
        Game game = Game.from(FEN.START_POSITION);
        rootMoveEvaluationCollection.setGame(game);
        rootMoveEvaluationCollection.beforeSearch();

        rootMoveEvaluationCollection.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move1, 1000, Bound.EXACT));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move2, 2000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move3, 3000, Bound.EXACT));

        rootMoveEvaluationCollection.afterSearchByDepth();

        RootMoveEvaluation maxEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();
        assertEquals(move3, maxEvaluation.move());
        assertEquals(3000, maxEvaluation.evaluation());
        assertEquals(Bound.EXACT, maxEvaluation.bound());
    }

    @Test
    public void test02() {
        Game game = Game.from(FEN.START_POSITION).mirror();
        rootMoveEvaluationCollection.setGame(game);
        rootMoveEvaluationCollection.beforeSearch();
        rootMoveEvaluationCollection.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move1, 1000, Bound.EXACT));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move2, 2000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move3, 3000, Bound.EXACT));

        rootMoveEvaluationCollection.afterSearchByDepth();

        RootMoveEvaluation minEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();
        assertEquals(move1, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(Bound.EXACT, minEvaluation.bound());
    }

    @Test
    public void test03() {
        Game game = Game.from(FEN.START_POSITION);
        rootMoveEvaluationCollection.setGame(game);
        rootMoveEvaluationCollection.beforeSearch();
        rootMoveEvaluationCollection.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move1, 1000, Bound.LOWER_BOUND));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move2, 1000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move3, 1000, Bound.UPPER_BOUND));

        rootMoveEvaluationCollection.afterSearchByDepth();

        // Move1 es más prometedor que el resto, dado que maximizamos y es LOWER_BOUND, por lo que es el mejor
        RootMoveEvaluation maxEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();
        assertEquals(move1, maxEvaluation.move());
        assertEquals(1000, maxEvaluation.evaluation());
        assertEquals(Bound.LOWER_BOUND, maxEvaluation.bound());
    }

    @Test
    public void test04() {
        Game game = Game.from(FEN.START_POSITION).mirror();
        rootMoveEvaluationCollection.setGame(game);
        rootMoveEvaluationCollection.beforeSearch();
        rootMoveEvaluationCollection.beforeSearchByDepth();

        final Move move1 = createSimpleKnightMove(PiecePositioned.of(Square.a2, Piece.PAWN_WHITE), PiecePositioned.of(Square.a3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move1, 1000, Bound.LOWER_BOUND));

        final Move move2 = createSimpleKnightMove(PiecePositioned.of(Square.b2, Piece.PAWN_WHITE), PiecePositioned.of(Square.b3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move2, 1000, Bound.EXACT));

        final Move move3 = createSimpleKnightMove(PiecePositioned.of(Square.c2, Piece.PAWN_WHITE), PiecePositioned.of(Square.c3, null));
        rootMoveEvaluationCollection.save(new RootMoveEvaluation(move3, 1000, Bound.UPPER_BOUND));

        rootMoveEvaluationCollection.afterSearchByDepth();

        // Move1 es más prometedor que el resto, dado que minimizamos y es UPPER_BOUND, por lo que es el mejor
        RootMoveEvaluation minEvaluation = rootMoveEvaluationCollection.getBestRootMoveEvaluation();
        assertEquals(move3, minEvaluation.move());
        assertEquals(1000, minEvaluation.evaluation());
        assertEquals(Bound.UPPER_BOUND, minEvaluation.bound());
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
