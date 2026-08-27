package net.chesstango.search.smart;

/**
 * @author Mauricio Coria
 */
@FunctionalInterface
public interface AlphaBetaFilter {
    int alphaBeta(final int currentPly, final int alpha, final int beta);
}
