package net.chesstango.search.smart.sorters;

import lombok.Setter;
import net.chesstango.evaluation.GameEvaluatorCacheRead;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class GameEvaluatorCacheReadMock implements GameEvaluatorCacheRead {
    @Setter
    private Map<Long, Integer> cache;

    @Override
    public Integer readFromCache(long hash) {
        return cache.get(hash);
    }
}
