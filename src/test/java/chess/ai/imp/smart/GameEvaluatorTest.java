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
    public void testQueenWhiteCheckMate() {
        Game game = getGame("rnbqkbnr/2pppQpp/8/pp4N1/8/4P3/PPPP1PPP/RNB1KB1R b KQkq - 0 5");

        int mateDepth1 = evaluator.evaluate(game, 1);

        int mateDepth3 = evaluator.evaluate(game, 3);

        Assert.assertTrue("Mate now is better than Mate later", mateDepth1 > mateDepth3);
    }


    @Test
    public void testDraw() {
        Game game = getGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1 ");

        int eval = evaluator.evaluate(game, 1);

        Assert.assertEquals("Draw", 0, eval);
    }

    @Test
    public void testMateCheckAndDraw() {
        Game mate = getGame("4Q2k/8/7K/8/8/8/8/8 b - - 0 1 ");
        Game check = getGame("7k/8/7K/8/3Q4/8/8/8 b - - 0 1 ");
        Game draw = getGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1 ");

        int mateEval = evaluator.evaluate(mate, 1);
        int checkEval = evaluator.evaluate(check, 1);
        int drawEval = evaluator.evaluate(draw, 1);

        Assert.assertTrue("Mate is better than check", mateEval > checkEval);
        Assert.assertTrue("Check is better than draw", checkEval > drawEval);
    }

    @Test
    public void testCloseToPromotion() {
        Game promotionInTwoMoves = getGame("7k/8/P7/8/8/8/8/7K w - - 0 1");
        Game promotionInOneMoves = getGame("7k/P7/8/8/8/8/8/7K w - - 0 1 ");

        int evalPromotionInTwoMoves = evaluator.evaluate(promotionInTwoMoves, 1);
        int evalPromotionInOneMoves = evaluator.evaluate(promotionInOneMoves, 1);

        Assert.assertTrue("Promotion in One move is better than promotion in two moves", evalPromotionInOneMoves > evalPromotionInTwoMoves);
    }

    protected Game getGame(String string) {
        GameBuilder builder = new GameBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getResult();
    }
}
