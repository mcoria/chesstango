package net.chesstango.evaluation;

/**
 *
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface EvaluatorCacheRead {
    Integer readFromCache(long hash);
}
