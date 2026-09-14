package net.chesstango.search.alphabeta;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface AlphaBetaFilter {
    int alphaBeta(final int currentPly, final int alpha, final int beta);
}
