package net.chesstango.search.smart.statistics.evaluation;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.evaluator.EvaluatorCacheArray;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.SearchListener;

import java.util.Set;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluationCounters implements Acceptor, SearchListener {
    private long evaluationsCounter;

    @Setter
    @Accessors(chain = true)
    private EvaluatorCacheArray evaluatorCacheArray;

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
        long evaluationsCacheHitsCounter = evaluatorCacheArray != null ? evaluatorCacheArray.getEvaluationsCacheHitsCounter() : 0;
        long readFromCacheCounter = evaluatorCacheArray != null ? evaluatorCacheArray.getReadFromCacheCounter() : 0;
        long readFromCacheHitsCounter = evaluatorCacheArray != null ? evaluatorCacheArray.getReadFromCacheHitsCounter() : 0;
        int fillPercentage = evaluatorCacheArray != null ? evaluatorCacheArray.getFillPercentage() : 0;
        return new EvaluationStatistics(evaluationsCounter, evaluationsCacheHitsCounter, readFromCacheCounter, readFromCacheHitsCounter, fillPercentage, evaluations);
    }

}
