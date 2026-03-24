package net.chesstango.search.smart.alphabeta.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Mauricio Coria
 */
public class EvaluatorStatisticsCollector implements Evaluator, SearchByCycleListener {

    @Setter
    @Getter
    @Accessors(chain = true)
    private Evaluator imp;

    @Setter
    @Accessors(chain = true)
    private EvaluationCounters evaluationsCounters;

    @Setter
    @Accessors(chain = true)
    private boolean trackEvaluations;

    private Game game;

    private Set<EvaluationEntry> evaluations;

    @Override
    public void setGame(Game game) {
        this.game = game;
        this.imp.setGame(game);
    }

    @Override
    public int evaluate() {
        int evaluation = imp.evaluate();
        evaluationsCounters.increaseEvaluationsCounter();
        if (trackEvaluations) {
            long hash = game.getPosition().getZobristHash();
            evaluations.add(new EvaluationEntry(hash, evaluation));
        }
        return evaluation;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        if (trackEvaluations) {
            evaluations = new LinkedHashSet<>();
        }
    }

    @Override
    public void afterSearch() {
        evaluationsCounters.setEvaluations(evaluations);
    }

}
