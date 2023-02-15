package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;

public class DefaultGameEvaluator implements GameEvaluator {

    private GameEvaluator imp = new GameEvaluatorImp02();

    @Override
    public int evaluate(Game game) {
        return imp.evaluate(game);
    }
}
