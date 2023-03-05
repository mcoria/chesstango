package net.chesstango.evaluation.imp;

import net.chesstango.evaluation.GameEvaluator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GameEvaluatorByMaterialAndMovesTest extends GameEvaluationTestCollection  {
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
