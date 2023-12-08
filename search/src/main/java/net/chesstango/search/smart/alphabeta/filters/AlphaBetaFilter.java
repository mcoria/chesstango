package net.chesstango.search.smart.alphabeta.filters;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter {

    long maximize(final int currentPly, final int alpha, final int beta);

    long minimize(final int currentPly, final int alpha, final int beta);

}
