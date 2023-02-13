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
public class GameEvaluatorImp02Test extends GameEvaluationTestCollection{

    private GameEvaluatorImp02 evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluatorImp02();
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


}
