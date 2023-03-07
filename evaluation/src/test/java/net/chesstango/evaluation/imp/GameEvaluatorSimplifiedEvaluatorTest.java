package net.chesstango.evaluation.imp;

import net.chesstango.board.Square;
import net.chesstango.evaluation.GameEvaluator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static net.chesstango.evaluation.imp.GameEvaluatorSimplifiedEvaluator.PAWN_BLACK_VALUES;
import static net.chesstango.evaluation.imp.GameEvaluatorSimplifiedEvaluator.PAWN_WHITE_VALUES;

public class GameEvaluatorSimplifiedEvaluatorTest extends GameEvaluationTestCollection{

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
    public void testPawnValues(){
        Assert.assertEquals(5, PAWN_WHITE_VALUES[Square.a2.toIdx()] );
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.b2.toIdx()] );
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.c2.toIdx()] );
        Assert.assertEquals(-20, PAWN_WHITE_VALUES[Square.d2.toIdx()] );
        Assert.assertEquals(-20, PAWN_WHITE_VALUES[Square.e2.toIdx()] );
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.f2.toIdx()] );
        Assert.assertEquals(10, PAWN_WHITE_VALUES[Square.g2.toIdx()] );
        Assert.assertEquals(5, PAWN_WHITE_VALUES[Square.h2.toIdx()] );

        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.a7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.b7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.c7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.d7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.e7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.f7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.g7.toIdx()] );
        Assert.assertEquals(50, PAWN_WHITE_VALUES[Square.h7.toIdx()] );

        /*
        for (int i = 0; i < 64; i++) {
            System.out.printf("%d,", -PAWN_WHITE_VALUES[Square.getSquareByIdx(i).getMirrorSquare().toIdx()]);
            if( (i + 1) % 8 == 0 ){
                System.out.println ("\tRank " + (i + 1));
            }
        }*/

        for (Square square: Square.values()) {
            Assert.assertEquals(PAWN_WHITE_VALUES[square.toIdx()], -PAWN_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
        }
    }

}
