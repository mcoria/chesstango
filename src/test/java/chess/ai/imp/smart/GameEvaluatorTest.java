package chess.ai.imp.smart;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameEvaluatorTest {

    private GameEvaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluator();
    }

    @Test
    public void testInfinities() {
        Assert.assertEquals("+infinite is equals to  (-1) * -infinite ", GameEvaluator.INFINITE_POSITIVE, (-1) * GameEvaluator.INFINITE_NEGATIVE);
        Assert.assertEquals("-infinite is equals to  (-1) * +infinite ", GameEvaluator.INFINITE_NEGATIVE, (-1) * GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.WHITE_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.BLACK_LOST);

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.BLACK_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.WHITE_LOST);
    }


    @Test
    public void testDraw() {
        Game game = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1 ");

        int eval = evaluator.evaluate(game);

        Assert.assertEquals("Draw", 0, eval);
    }

    @Test
    public void testBlackMateInCheckAndDraw() {
        Game mate = FENDecoder.loadGame("4Q2k/8/7K/8/8/8/8/8 b - - 0 1");       // Black is in Mate
        Game check = FENDecoder.loadGame("2q4k/8/7K/8/3Q4/8/8/8 b - - 0 1");    // Black is in Check
        Game draw = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");       // Draw

        int mateEval = evaluator.evaluate(mate);
        int checkEval = evaluator.evaluate(check);
        int drawEval = evaluator.evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_LOST, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_WON, mateEval);

        Assert.assertTrue(mateEval > checkEval);

        Assert.assertTrue(checkEval > drawEval);
    }

    @Test
    public void testWhiteInMateCheckAndDraw() {
        Game mate = FENDecoder.loadGame("8/8/8/8/8/7k/8/4q2K w - - 0 1");        // White is in Mate
        Game check = FENDecoder.loadGame("8/8/8/3q4/8/7k/8/2Q4K w - - 0 1");     // White is in Check
        Game draw = FENDecoder.loadGame("6q1/8/8/8/8/7k/8/7K w - - 0 1");         // Draw

        int mateEval = evaluator.evaluate(mate);
        int checkEval = evaluator.evaluate(check);
        int drawEval = evaluator.evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_WON, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_LOST, mateEval);

        Assert.assertTrue(mateEval < checkEval);

        Assert.assertTrue(checkEval < drawEval);
    }

    @Test
    public void testCloseToPromotion() {
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");
        Game promotionInOneMoves = FENDecoder.loadGame("7k/P7/8/8/8/8/8/7K w - - 0 1 ");

        int evalPromotionInTwoMoves = evaluator.evaluate(promotionInTwoMoves);
        int evalPromotionInOneMoves = evaluator.evaluate(promotionInOneMoves);

        Assert.assertTrue("Promotion in One move is better than promotion in two moves", evalPromotionInOneMoves > evalPromotionInTwoMoves);
    }

    @Test
    public void testComparations() {
        Game game = FENDecoder.loadGame("1k6/3Q4/6P1/1pP5/8/1B3P2/3R4/6K1 w - - 0 1");

        int eval = evaluator.evaluate(game);

        Assert.assertTrue("White has not won yet", eval != GameEvaluator.WHITE_WON);
        Assert.assertTrue("White has not lost yet", eval != GameEvaluator.WHITE_LOST);
        Assert.assertTrue("Black has not won yet", eval != GameEvaluator.BLACK_WON);
        Assert.assertTrue("Black has not lost yet", eval != GameEvaluator.BLACK_LOST);
        Assert.assertTrue("White has a better position than Black", eval > 0);
    }

}
