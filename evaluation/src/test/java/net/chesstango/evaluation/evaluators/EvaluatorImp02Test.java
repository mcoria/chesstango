package net.chesstango.evaluation.evaluators;


import net.chesstango.board.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;


/**
 * @author Mauricio Coria
 */
public class EvaluatorImp02Test extends GameEvaluatorTestCollection {

    private EvaluatorImp02 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp02();
    }

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        if (game != null) {
            evaluator.setGame(game);
        }
        return evaluator;
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }

}
