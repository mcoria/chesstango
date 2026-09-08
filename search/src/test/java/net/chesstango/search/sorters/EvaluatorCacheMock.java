package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.evalcache.EvaluatorCache;
import net.chesstango.search.smart.evalcache.EvaluatorCacheEntry;

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
    public EvaluatorCacheEntry read(long hash) {
        Integer result = cache.get(hash);
        if (result != null) {
            return new EvaluatorCacheEntry(hash, result, 0);
        }
        return null;
    }

    @Override
    public EvaluatorCacheEntry write(long hash, int evaluation) {
        cache.put(hash, evaluation);
        return new EvaluatorCacheEntry(hash, evaluation, 0);
    }
}
