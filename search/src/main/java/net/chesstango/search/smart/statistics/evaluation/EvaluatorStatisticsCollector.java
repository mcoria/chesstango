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
@Setter
@Accessors(chain = true)
public class EvaluatorStatisticsCollector implements Evaluator, Acceptor {

    @Getter
    private Evaluator evaluator;

    private EvaluationCounters evaluationsCounters;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void setGame(Game game) {
        this.evaluator.setGame(game);
    }

    @Override
    public int evaluate() {
        evaluationsCounters.increaseEvaluationsCounter();
        return evaluator.evaluate();
    }

}
