package net.chesstango.search.smart.statistics.evalcache;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.evalcache.EvaluatorCache;
import net.chesstango.search.smart.evalcache.EvaluatorCacheEntry;
import net.chesstango.search.smart.statistics.evaluator.EvaluatorCounters;

/**
 * @author Mauricio Coria
 */
@Setter
@Accessors(chain = true)
public class EvaluatorCacheStatisticsComparatorCollector implements EvaluatorCache, Acceptor {

    @Getter
    private EvaluatorCache evaluatorCache;

    private EvaluatorCounters evaluationsCounters;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public EvaluatorCacheEntry read(long hash) {
        EvaluatorCacheEntry evaluatorCacheEntry = evaluatorCache.read(hash);
        if (evaluatorCacheEntry != null) {
            //evaluationsCounters.increaseReadFromCacheHitsCounter();
        }
        return evaluatorCacheEntry;
    }

    @Override
    public EvaluatorCacheEntry write(long hash, int evaluation) {
        throw new UnsupportedOperationException("Write should not be invoked here");
    }
}
