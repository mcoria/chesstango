package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;

/**
 * @author Mauricio Coria
 */
public class DefaultGameEvaluator implements GameEvaluator {

    private GameEvaluator imp = new GameEvaluatorSEandImp02();

    @Override
    public int evaluate(Game game) {
        return imp.evaluate(game);
    }
}
