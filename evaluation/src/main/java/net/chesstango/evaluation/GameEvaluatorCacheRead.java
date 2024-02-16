package net.chesstango.evaluation;

/**
 *
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface GameEvaluatorCacheRead {
    Integer readFromCache(long hash);
}
