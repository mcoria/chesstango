package net.chesstango.search.smart.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

/**
 * @author Mauricio Coria
 */
public class EvaluatorStatisticsCollector implements Evaluator, Acceptor {

    @Setter
    @Getter
    @Accessors(chain = true)
    private Evaluator imp;

    @Setter
    @Accessors(chain = true)
    private EvaluationCounters evaluationsCounters;

    @Override
    public void setGame(Game game) {
        this.imp.setGame(game);
    }

    @Override
    public int evaluate() {
        int evaluation = imp.evaluate();
        evaluationsCounters.increaseEvaluationsCounter();
        return evaluation;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


}
