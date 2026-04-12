package net.chesstango.search.smart.alphabeta.statistics.evaluation;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.Set;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluationCounters implements Acceptor, SearchByCycleListener {
    private long evaluationsCounter;

    @Setter
    @Accessors(chain = true)
    private EvaluatorCache evaluatorCache;

    @Setter
    @Accessors(chain = true)
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
        long evaluationsCacheHitsCounter = evaluatorCache != null ? evaluatorCache.getEvaluationsCacheHitsCounter() : 0;
        long readFromCacheCounter = evaluatorCache != null ? evaluatorCache.getReadFromCacheCounter() : 0;
        long readFromCacheHitsCounter = evaluatorCache != null ? evaluatorCache.getReadFromCacheHitsCounter() : 0;
        int fillPercentage = evaluatorCache != null ? evaluatorCache.getFillPercentage() : 0;
        return new EvaluationStatistics(evaluationsCounter, evaluationsCacheHitsCounter, readFromCacheCounter, readFromCacheHitsCounter, fillPercentage, evaluations);
    }

}
