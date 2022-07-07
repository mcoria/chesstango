package net.chesstango.ai.imp.smart.evaluation.imp;

import net.chesstango.ai.imp.smart.evaluation.GameEvaluator;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public abstract class GameEvaluationTestCollection {

    protected abstract GameEvaluator getEvaluator();


    public void testBlackInMate() {
        Game mate = FENDecoder.loadGame("4Q2k/8/7K/8/8/8/8/8 b - - 0 1");       // Black is in Mate
        Game check = FENDecoder.loadGame("2q4k/8/7K/8/3Q4/8/8/8 b - - 0 1");    // Black is in Check
        Game draw = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");       // Draw

        int mateEval = getEvaluator().evaluate(mate);
        int checkEval = getEvaluator().evaluate(check);
        int drawEval = getEvaluator().evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_LOST, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_WON, mateEval);

        Assert.assertTrue(mateEval > checkEval);

        Assert.assertTrue(mateEval > drawEval);
    }


    public void testWhiteInMate() {
        Game mate = FENDecoder.loadGame("8/8/8/8/8/7k/8/4q2K w - - 0 1");        // White is in Mate
        Game check = FENDecoder.loadGame("7k/8/8/3q4/8/8/8/2Q4K w - - 0 1");     // White is in Check
        Game draw = FENDecoder.loadGame("6q1/8/8/8/8/7k/8/7K w - - 0 1");         // Draw

        int mateEval = getEvaluator().evaluate(mate);
        int checkEval = getEvaluator().evaluate(check);
        int drawEval = getEvaluator().evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_WON, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_LOST, mateEval);

        Assert.assertTrue(mateEval < checkEval);

        Assert.assertTrue(mateEval < drawEval);
    }


    public void testCloseToPromotion() {
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");
        Game promotionInOneMoves = FENDecoder.loadGame("7k/P7/8/8/8/8/8/7K w - - 0 1 ");

        int evalPromotionInTwoMoves = getEvaluator().evaluate(promotionInTwoMoves);
        int evalPromotionInOneMoves = getEvaluator().evaluate(promotionInOneMoves);

        Assert.assertTrue("Promotion in One move is better than promotion in two moves", evalPromotionInOneMoves > evalPromotionInTwoMoves);
    }


    public void testComparatives() {
        Game game = FENDecoder.loadGame("1k6/3Q4/6P1/1pP5/8/1B3P2/3R4/6K1 w - - 0 1");

        int eval = getEvaluator().evaluate(game);

        Assert.assertTrue("White has not won yet", eval != GameEvaluator.WHITE_WON);
        Assert.assertTrue("White has not lost yet", eval != GameEvaluator.WHITE_LOST);
        Assert.assertTrue("Black has not won yet", eval != GameEvaluator.BLACK_WON);
        Assert.assertTrue("Black has not lost yet", eval != GameEvaluator.BLACK_LOST);
        Assert.assertTrue("White has a better position than Black", eval > 0);
    }
}
