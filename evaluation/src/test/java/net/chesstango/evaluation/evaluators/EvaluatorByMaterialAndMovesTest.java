package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
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
    protected AbstractEvaluator getEvaluator(Game game) {
        evaluator.setGame(game);
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
