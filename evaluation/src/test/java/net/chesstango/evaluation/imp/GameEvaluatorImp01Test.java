package net.chesstango.evaluation.imp;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class GameEvaluatorImp01Test extends GameEvaluationTestCollection{

    private GameEvaluatorImp01 evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluatorImp01();
    }

    @Override
    protected GameEvaluator getEvaluator() {
        return evaluator;
    }

    @Test
    public void testBlackInMate() {
        super.testBlackInMate();
    }

    @Test
    public void testWhiteInMate() {
        super.testWhiteInMate();
    }

    @Test
    public void testComparatives() {
        super.testComparatives();
    }

    @Test
    public void testDraw() {
        Game game = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");

        int eval = evaluator.evaluate(game);

        Assert.assertEquals("Draw", 0, eval);
    }

    @Test
    public void test_evaluateByMoves_white() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int eval1 = evaluator.evaluateByMoves(game);


        game = FENDecoder.loadGame("rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        int eval2 = evaluator.evaluateByMoves(game);

        Assert.assertTrue(eval1 > 0);
        Assert.assertTrue(eval2 > 0);

        Assert.assertTrue(eval2 > eval1);
    }

    @Test
    public void test_evaluateByMoves_black() {
        Game game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        int eval1 = evaluator.evaluateByMoves(game);


        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
        int eval2 = evaluator.evaluateByMoves(game);

        Assert.assertTrue(eval1 < 0);
        Assert.assertTrue(eval2 < 0);

        Assert.assertTrue(eval2 < eval1);
    }

}
