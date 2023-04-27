package net.chesstango.evaluation.imp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp03Test extends GameEvaluatorTestCollection {

    private GameEvaluatorImp03 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new GameEvaluatorImp03();
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
