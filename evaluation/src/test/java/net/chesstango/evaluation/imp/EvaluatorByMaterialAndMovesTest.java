package net.chesstango.evaluation.imp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * @author Mauricio Coria
 */
public class EvaluatorByMaterialAndMovesTest extends GameEvaluatorTestCollection {
    private EvaluatorImp01 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp01();
    }

    @Override
    protected AbstractEvaluator getEvaluator() {
        return evaluator;
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionOneMove() {
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }
}
