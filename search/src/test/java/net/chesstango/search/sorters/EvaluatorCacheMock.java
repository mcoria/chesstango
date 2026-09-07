package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.search.smart.evaluator.EvaluatorCache;

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
    public Integer readFromCache(long hash) {
        return cache.get(hash);
    }
}
