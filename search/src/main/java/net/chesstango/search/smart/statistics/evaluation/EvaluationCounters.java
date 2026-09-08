package net.chesstango.search.smart.statistics.evaluation;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evaluator.EvaluatorCacheArray;

import java.util.Set;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluationCounters implements Acceptor, SearchListener {
    private long evaluationsCounter;

    @Getter
    private long evaluationsCacheHitsCounter;

    /**
     * Cuantos intentos de lectura de cache
     */
    @Getter
    private long readFromCacheCounter;

    /**
     * Cuantos intentos de lectura de cache exitosos
     */
    @Getter
    private long readFromCacheHitsCounter;

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
        evaluationsCacheHitsCounter = 0;
        readFromCacheCounter = 0;
        readFromCacheHitsCounter = 0;
    }

    public void increaseEvaluationsCounter() {
        evaluationsCounter++;
    }


    public EvaluationStatistics getEvaluationStatistics() {
        int fillPercentage = evaluatorCacheArray != null ? evaluatorCacheArray.getFillPercentage() : 0;
        return new EvaluationStatistics(evaluationsCounter, evaluationsCacheHitsCounter, readFromCacheCounter, readFromCacheHitsCounter, fillPercentage, evaluations);
    }

}
