package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorImp05.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp05Test extends GameEvaluatorTestCollection {

    private EvaluatorImp05 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp05();
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
        assertEquals(-4866, evaluator.evaluateByPosition());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(4866, evaluator.evaluateByPosition());
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = FENDecoder.loadGame("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(0, evaluator.evaluateByPosition());
    }


    @Test
    public void testEvaluation01() {
        Game game = FENDecoder.loadGame("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c4);
        game.executeMove(Square.h8, Square.g8);

        assertEquals(55752, evaluator.evaluate());
    }

}
