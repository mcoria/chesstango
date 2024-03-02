package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.evaluation.GameEvaluatorCacheRead;

import java.util.Map;

/**
 * @author Mauricio Coria
 */
@Getter
@Setter
@Accessors(chain = true)
public class GameEvaluatorCacheReadMock implements GameEvaluatorCacheRead {

    private Map<Long, Integer> cache;

    @Override
    public Integer readFromCache(long hash) {
        return cache.get(hash);
    }
}
