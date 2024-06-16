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
        assertEquals(5, PAWN_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(-20, PAWN_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(-20, PAWN_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(5, PAWN_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(50, PAWN_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.h7.toIdx()]);

        assertEquals(64, PAWN_WHITE_VALUES.length);
    }

    @Test
    public void testKnightValues() {
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(5, KNIGHT_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(5, KNIGHT_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h7.toIdx()]);

        assertEquals(64, KNIGHT_WHITE_VALUES.length);
    }

    @Test
    public void testBishopValues() {
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(5, BISHOP_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(5, BISHOP_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(-10, BISHOP_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.h7.toIdx()]);

        assertEquals(64, BISHOP_WHITE_VALUES.length);
    }

    @Test
    public void testRookValues() {
        assertEquals(-5, ROOK_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(-5, ROOK_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(5, ROOK_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(5, ROOK_WHITE_VALUES[Square.h7.toIdx()]);

        assertEquals(64, ROOK_WHITE_VALUES.length);
    }


    @Test
    public void testQueenValues() {
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(5, QUEEN_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(-10, QUEEN_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.h7.toIdx()]);


        assertEquals(64, QUEEN_WHITE_VALUES.length);
    }

    @Test
    public void testKingValues() {
        assertEquals(20, KING_WHITE_VALUES[Square.a2.toIdx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.b2.toIdx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.c2.toIdx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.d2.toIdx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.e2.toIdx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.f2.toIdx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.g2.toIdx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.h2.toIdx()]);

        assertEquals(-30, KING_WHITE_VALUES[Square.a7.toIdx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.b7.toIdx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.c7.toIdx()]);
        assertEquals(-50, KING_WHITE_VALUES[Square.d7.toIdx()]);
        assertEquals(-50, KING_WHITE_VALUES[Square.e7.toIdx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.f7.toIdx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.g7.toIdx()]);
        assertEquals(-30, KING_WHITE_VALUES[Square.h7.toIdx()]);


        assertEquals(64, KING_WHITE_VALUES.length);
    }

    @Test
    public void testEvaluateByPosition() {
        Game game = FENDecoder.loadGame("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");
        evaluator.setGame(game);
        assertEquals(-90, evaluator.evaluateByPosition());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(90, evaluator.evaluateByPosition());
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

        assertEquals(165300, evaluator.evaluate());
    }

    @Test
    public void testEvaluation02() {
        Game game = FENDecoder.loadGame("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c2);
        game.executeMove(Square.h8, Square.g8);
        game.executeMove(Square.c2, Square.d4);


        assertEquals(168750, evaluator.evaluate());
    }

    @Test
    public void testEvaluation03() {
        Game game = FENDecoder.loadGame("r2qnrnk/p2b2b1/1p1p2pp/2pPpp2/1PP1P3/PRNBB3/3QNPPP/5RK1 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.f2, Square.f4);
        game.executeMove(Square.c5, Square.b4);
        game.executeMove(Square.b3, Square.b4);
        game.executeMove(Square.d8, Square.e7);
        game.executeMove(Square.f4, Square.e5);
        game.executeMove(Square.d6, Square.e5);


        assertEquals(26000, evaluator.evaluate());
    }

    @Test
    public void testEvaluation04() {
        Game game = FENDecoder.loadGame("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.c3, Square.d5);
        game.executeMove(Square.f6, Square.d5);
        game.executeMove(Square.f1, Square.f8);
        game.executeMove(Square.g8, Square.f8);
        game.executeMove(Square.e4, Square.d5);


        assertEquals(19515, evaluator.evaluate());
    }

    @Test
    public void testEvaluation05() {
        Game game = FENDecoder.loadGame("1r4k1/pp3p1p/3p2pB/4r3/P7/1P1P1K2/3R2BP/8 w - - 0 31");
        evaluator.setGame(game);


        assertEquals(-52230, evaluator.evaluate());
    }

}
