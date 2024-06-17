package net.chesstango.evaluation;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorImp05;

/**
 * @author Mauricio Coria
 */
@Getter
public class DefaultEvaluator implements Evaluator {

    private final Evaluator imp = new EvaluatorImp05();

    @Override
    public void setGame(Game game) {
        imp.setGame(game);
    }

    @Override
    public int evaluate() {
        return imp.evaluate();
    }
}
