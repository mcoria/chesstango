package net.chesstango.evaluation.imp;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static net.chesstango.evaluation.imp.GameEvaluatorSimplifiedEvaluator.*;

public class GameEvaluatorSimplifiedEvaluatorTest extends GameEvaluationTestCollection {

    private GameEvaluatorSimplifiedEvaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluatorSimplifiedEvaluator();
    }

    @Override
    protected GameEvaluator getEvaluator() {
        return evaluator;
    }

    @Test
    public void testPawnValues() {
        Assert.assertEquals(5, PAWN_WHITE_VALUES[Square.a2.toIdx()]);
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.b2.toIdx()]);
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.c2.toIdx()]);
        Assert.assertEquals(-20, PAWN_WHITE_VALUES[Square.d2.toIdx()]);
        Assert.assertEquals(-20, PAWN_WHITE_VALUES[Square.e2.toIdx()]);
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.f2.toIdx()]);
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.g2.toIdx()]);
        Assert.assertEquals(5, PAWN_WHITE_VALUES[Square.h2.toIdx()]);

        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.a7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.b7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.c7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.d7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.e7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.f7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.g7.toIdx()]);
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.h7.toIdx()]);

        Assert.assertTrue(PAWN_WHITE_VALUES.length == 64 );
        Assert.assertTrue(PAWN_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            Assert.assertEquals(PAWN_WHITE_VALUES[square.toIdx()], -PAWN_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
    }

    @Test
    public void testKnightValues() {
        Assert.assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a2.toIdx()]);
        Assert.assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b2.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.c2.toIdx()]);
        Assert.assertEquals(5, KNIGHT_WHITE_VALUES[Square.d2.toIdx()]);
        Assert.assertEquals(5, KNIGHT_WHITE_VALUES[Square.e2.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.f2.toIdx()]);
        Assert.assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g2.toIdx()]);
        Assert.assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h2.toIdx()]);

        Assert.assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a7.toIdx()]);
        Assert.assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b7.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.c7.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.d7.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.e7.toIdx()]);
        Assert.assertEquals(0, KNIGHT_WHITE_VALUES[Square.f7.toIdx()]);
        Assert.assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g7.toIdx()]);
        Assert.assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h7.toIdx()]);


        /*
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                System.out.printf("%d,", -KNIGHT_WHITE_VALUES[Square.getSquare(file, rank).toIdx()]);
                if (file == 7) {
                    System.out.println("\tRank");
                }
            }
        }
         */

        Assert.assertTrue(KNIGHT_WHITE_VALUES.length == 64 );
        Assert.assertTrue(KNIGHT_BLACK_VALUES.length == 64 );

        for (Square square : Square.values()) {
            System.out.printf("Comparing %s(%d) and %s(%d)\n", square, KNIGHT_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), KNIGHT_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            Assert.assertEquals(KNIGHT_WHITE_VALUES[square.toIdx()], -KNIGHT_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }

    }

    @Test
    public void testPawnPositions() {
        Game game = FENDecoder.loadGame("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");
        Assert.assertEquals(1 * (5 + 10 + 10 - 20)  //White
                                + (-1) * (5 + 10 + 0 + 20 + 10 + 10 + 5), evaluator.evaluateByPosition(game));
    }

}
