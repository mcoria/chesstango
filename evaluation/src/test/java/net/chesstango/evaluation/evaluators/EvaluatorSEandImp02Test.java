package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorSimplifiedEvaluator.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EvaluatorSEandImp02Test extends GameEvaluatorTestCollection {

    private EvaluatorSEandImp02 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorSEandImp02();
    }

    @Override
    protected AbstractEvaluator getEvaluator() {
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

        assertTrue(PAWN_WHITE_VALUES.length == 64 );
        assertTrue(PAWN_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            assertEquals(PAWN_WHITE_VALUES[square.toIdx()], -PAWN_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
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

        assertTrue(KNIGHT_WHITE_VALUES.length == 64 );
        assertTrue(KNIGHT_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, KNIGHT_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), KNIGHT_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(KNIGHT_WHITE_VALUES[square.toIdx()], -KNIGHT_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }

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

        assertTrue(BISHOP_WHITE_VALUES.length == 64 );
        assertTrue(BISHOP_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, BISHOPS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), BISHOPS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(BISHOP_WHITE_VALUES[square.toIdx()], -BISHOP_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
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

        assertTrue(ROOK_WHITE_VALUES.length == 64 );
        assertTrue(ROOK_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, ROOKS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), ROOKS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(ROOK_WHITE_VALUES[square.toIdx()], -ROOK_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
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


        assertTrue(QUEEN_WHITE_VALUES.length == 64 );
        assertTrue(QUEEN_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, QUEENS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), QUEENS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(QUEEN_WHITE_VALUES[square.toIdx()], -QUEEN_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
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

        /*
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                System.out.printf("%d,", -KING_WHITE_VALUES[Square.getSquare(file, rank).getMirrorSquare().toIdx()]);
                if (file == 7) {
                    System.out.println("\t // Rank " + (rank + 1));
                }
            }
        }
         */



        assertTrue(KING_WHITE_VALUES.length == 64 );
        assertTrue(KING_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, ROOKS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), ROOKS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(KING_WHITE_VALUES[square.toIdx()], -KING_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
    }

    @Test
    public void testEvaluateByPosition() {
        Game game = FENDecoder.loadGame("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");
        assertEquals(-90, evaluator.evaluateByPosition(game));

        Game gameMirror = game.mirror();
        assertEquals(90, evaluator.evaluateByPosition(gameMirror));
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = FENDecoder.loadGame("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4");
        assertEquals(0, evaluator.evaluate(game));

        Game gameMirror = game.mirror();
        assertEquals(0, evaluator.evaluateByPosition(gameMirror));
    }


}
