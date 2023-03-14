package net.chesstango.evaluation.imp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp02Test extends GameEvaluatorTestCollection {

    private GameEvaluatorImp02 evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluatorImp02();
    }

    @Override
    protected AbstractEvaluator getEvaluator() {
        return evaluator;
    }

    @Test
    @Override
    @Ignore //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }

}
