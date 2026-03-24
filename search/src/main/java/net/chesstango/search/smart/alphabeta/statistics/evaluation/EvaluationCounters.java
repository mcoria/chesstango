package net.chesstango.search.smart.alphabeta.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.Set;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluationCounters implements SearchByCycleListener {
    private long evaluationsCounter;

    private long cacheHitsCounter = 0;

    @Setter
    private Set<EvaluationEntry> evaluations;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        evaluationsCounter = 0;
    }

    public void increaseEvaluationsCounter() {
        evaluationsCounter++;
    }

    public EvaluationStatistics getEvaluationStatistics() {
        return new EvaluationStatistics(evaluationsCounter, cacheHitsCounter, evaluations);
    }
}
