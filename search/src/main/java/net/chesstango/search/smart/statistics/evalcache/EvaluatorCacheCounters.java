package net.chesstango.search.smart.statistics.evalcache;

import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchListener;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCacheArray;

/**
 *
 * @author Mauricio Coria
 */
public class EvaluatorCacheCounters implements Acceptor, SearchListener {
    private long evaluationsCounter;


    private long evaluationsCacheHitsCounter;

    /**
     * Cuantos intentos de lectura de cache
     */
    private long readFromCacheCounter;

    /**
     * Cuantos intentos de lectura de cache exitosos
     */
    private long readFromCacheHitsCounter;

    @Setter
    @Accessors(chain = true)
    private EvaluatorCacheArray evaluatorCacheArray;


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

    public void increaseReadFromCacheCounter() {
        readFromCacheCounter++;
    }

    public void increaseReadFromCacheHitsCounter() {
        readFromCacheHitsCounter++;
    }


    public EvaluatorCacheStatistics getEvaluatorCacheStatistics() {
        int fillPercentage = evaluatorCacheArray != null ? evaluatorCacheArray.getFillPercentage() : 0;
        return new EvaluatorCacheStatistics(evaluationsCounter, evaluationsCacheHitsCounter, readFromCacheCounter, readFromCacheHitsCounter, fillPercentage);
    }

}
