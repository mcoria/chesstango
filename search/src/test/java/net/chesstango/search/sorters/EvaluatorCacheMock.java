package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.evaluator.EvaluatorCache;
import net.chesstango.search.smart.evaluator.EvaluatorCacheEntry;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class EvaluatorCacheMock implements EvaluatorCache {

    private Map<Long, Integer> cache;

    @Override
    public EvaluatorCacheEntry readFromCache(long hash) {
        Integer result = cache.get(hash);
        if (result != null) {
            return new EvaluatorCacheEntry(hash, result, 0);
        }
        return null;
    }
}
