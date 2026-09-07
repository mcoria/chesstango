package net.chesstango.search.smart.evaluator;

/**
 *
 * @author Mauricio Coria
 */
public interface EvaluatorCache {
    EvaluatorCacheEntry readFromCache(long hash);
}
