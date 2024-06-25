package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorImp05.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterialImbalanceTest extends EvaluatorTestCollection {

    private EvaluatorByMaterialImbalance evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorByMaterialImbalance();
    }

    @Test
    public void testSymmetryOfPieceValues() {
        assertEquals(evaluator.getPieceValue(Piece.PAWN_WHITE), -evaluator.getPieceValue(Piece.PAWN_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.ROOK_WHITE), -evaluator.getPieceValue(Piece.ROOK_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.KNIGHT_WHITE), -evaluator.getPieceValue(Piece.KNIGHT_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.BISHOP_WHITE), -evaluator.getPieceValue(Piece.BISHOP_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.QUEEN_WHITE), -evaluator.getPieceValue(Piece.QUEEN_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.KING_WHITE), -evaluator.getPieceValue(Piece.KING_BLACK));
    }

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        if (game != null) {
            evaluator.setGame(game);
        }
        return evaluator;
    }

    @Test
    public void testPawnValues() {
        assertEquals(64, MG_PAWN_TBL.length);
        assertEquals(64, EG_PAWN_TBL.length);
    }

    @Test
    public void testKnightValues() {
        assertEquals(64, MG_KNIGHT_TBL.length);
        assertEquals(64, EG_KNIGHT_TBL.length);
    }

    @Test
    public void testBishopValues() {
        assertEquals(64, MG_BISHOP_TBL.length);
        assertEquals(64, EG_BISHOP_TBL.length);
    }

    @Test
    public void testRookValues() {
        assertEquals(64, MG_ROOK_TBL.length);
        assertEquals(64, EG_ROOK_TBL.length);
    }


    @Test
    public void testQueenValues() {
        assertEquals(64, MG_QUEEN_TBL.length);
        assertEquals(64, EG_QUEEN_TBL.length);
    }

    @Test
    public void testKingValues() {
        assertEquals(64, MG_KING_TBL.length);
        assertEquals(64, EG_KING_TBL.length);
    }

    @Test
    public void testEvaluateByPosition() {
        Game game = FENDecoder.loadGame("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");
        evaluator.setGame(game);
        assertEquals(-616, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(616, evaluator.evaluate());
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = FENDecoder.loadGame("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(0, evaluator.evaluate());
    }


    @Test
    public void testBishopPair() {
        Game game = FENDecoder.loadGame("k7/8/8/8/4B3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(331, evaluator.evaluate());

        game = FENDecoder.loadGame("k7/8/8/4B3/4B3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(661, evaluator.evaluate());

        game = FENDecoder.loadGame("k7/8/8/8/4b3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(-330, evaluator.evaluate());


        game = FENDecoder.loadGame("k7/8/8/4b3/4b3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(-661, evaluator.evaluate());
    }


    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionOneMove() {
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }

}
