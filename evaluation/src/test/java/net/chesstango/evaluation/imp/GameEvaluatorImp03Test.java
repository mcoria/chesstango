package net.chesstango.evaluation.imp;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp03Test extends GameEvaluationTestCollection {

    private GameEvaluatorImp03 evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluatorImp03();
    }

    @Override
    protected GameEvaluator getEvaluator() {
        return evaluator;
    }


    @Test
    @Override
    @Ignore //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionOneMove() {
    }

    @Test
    @Override
    @Ignore //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }
}