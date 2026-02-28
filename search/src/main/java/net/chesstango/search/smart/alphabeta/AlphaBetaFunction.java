package net.chesstango.search.smart.alphabeta;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface AlphaBetaFunction {
    long search(final int currentPly, final int alpha, final int beta);
}
