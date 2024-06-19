package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorImp05.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp06Test extends EvaluatorTestCollection {

    private EvaluatorImp06 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp06();
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
        assertEquals(-218262, evaluator.evaluateByPST());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(218262, evaluator.evaluateByPST());
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = FENDecoder.loadGame("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(0, evaluator.evaluateByPST());
    }


    @Test
    public void testEvaluation01() {
        Game game = FENDecoder.loadGame("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c4);
        game.executeMove(Square.h8, Square.g8);

        assertEquals(174890, evaluator.evaluate());
    }


    @Test
    public void testKnightPair() {
        evaluator.setWeighs(new int[]{0, 0, 0, 10, 0, 0});

        Game game = FENDecoder.loadGame("k7/8/8/4N3/8/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/4N3/4N3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(10, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/8/4n3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());


        game = FENDecoder.loadGame("k7/8/8/4n3/4n3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(-10, evaluator.evaluateByPairs());
    }

    @Test
    public void testBishopPair() {
        evaluator.setWeighs(new int[]{0, 0, 0, 0, 15, 0});

        Game game = FENDecoder.loadGame("k7/8/8/8/4B3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/4B3/4B3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(15, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/8/4b3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());


        game = FENDecoder.loadGame("k7/8/8/4b3/4b3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(-15, evaluator.evaluateByPairs());
    }

    @Test
    public void testRookPair() {
        evaluator.setWeighs(new int[]{0, 0, 0, 0, 0, 20});

        Game game = FENDecoder.loadGame("k7/8/8/8/4R3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/4R3/4R3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(20, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/8/4r3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluateByPairs());

        game = FENDecoder.loadGame("k7/8/8/4r3/4r3/8/8/K7 w - - 0 1");
        evaluator.setGame(game);
        assertEquals(-20, evaluator.evaluateByPairs());
    }

}
