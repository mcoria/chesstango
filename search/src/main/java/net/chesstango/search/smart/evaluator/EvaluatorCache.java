package net.chesstango.search.smart.evaluator;

/**
 *
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface EvaluatorCache {
    Integer readFromCache(long hash);
}
