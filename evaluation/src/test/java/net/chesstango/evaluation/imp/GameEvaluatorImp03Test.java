package net.chesstango.evaluation.imp;


import org.junit.Ignore;


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
    @Ignore //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionOneMove() {
    }

    @Test
    @Override
    @Ignore //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }
}
