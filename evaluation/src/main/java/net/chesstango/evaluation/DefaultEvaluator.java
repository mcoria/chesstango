package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;

/**
 * @author Mauricio Coria
 */
public class DefaultEvaluator implements GameEvaluator {

    private GameEvaluator imp = new EvaluatorSEandImp02();

    @Override
    public int evaluate(Game game) {
        return imp.evaluate(game);
    }
}
