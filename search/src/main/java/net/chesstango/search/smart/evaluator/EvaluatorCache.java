package net.chesstango.search.smart.evaluator;

/**
 *
 * @author Mauricio Coria
 */
public interface EvaluatorCache {
    EvaluatorCacheEntry read(long hash);

    EvaluatorCacheEntry write(long hash, int evaluation);
}
