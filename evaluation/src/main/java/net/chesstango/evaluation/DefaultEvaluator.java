package net.chesstango.evaluation;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;

/**
 * @author Mauricio Coria
 */
public class DefaultEvaluator implements Evaluator {

    @Getter
    private Evaluator imp = new EvaluatorImp04();


    @Override
    public void setGame(Game game) {
        imp.setGame(game);
    }

    @Override
    public int evaluate() {
        return imp.evaluate();
    }
}
