package net.chesstango.search.smart.evaluator;

/**
 *
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface EvaluatorCacheRead {
    Integer readFromCache(long hash);
}
