package net.chesstango.ai.imp.smart.evaluation;

import net.chesstango.ai.imp.smart.evaluation.imp.GameEvaluatorImp;
import org.junit.Assert;
import org.junit.Test;

public class GameEvaluatorTest {

    @Test
    public void testInfinities() {
        Assert.assertEquals("+infinite is equals to  (-1) * -infinite ", GameEvaluatorImp.INFINITE_POSITIVE, (-1) * GameEvaluatorImp.INFINITE_NEGATIVE);
        Assert.assertEquals("-infinite is equals to  (-1) * +infinite ", GameEvaluatorImp.INFINITE_NEGATIVE, (-1) * GameEvaluatorImp.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.WHITE_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.BLACK_LOST);

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.BLACK_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.WHITE_LOST);
    }
}
