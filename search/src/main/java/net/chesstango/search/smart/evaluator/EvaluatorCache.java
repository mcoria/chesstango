package net.chesstango.search.smart.evaluator;

/**
 *
 * @author Mauricio Coria
 */
public interface EvaluatorCache {
    Integer readFromCache(long hash);
}
