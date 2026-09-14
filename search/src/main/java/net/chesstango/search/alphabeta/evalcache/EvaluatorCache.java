package net.chesstango.search.alphabeta.evalcache;

/**
 *
 * @author Mauricio Coria
 */
public interface EvaluatorCache {
    EvaluatorCacheEntry read(long hash);

    EvaluatorCacheEntry write(long hash, int evaluation);
}
