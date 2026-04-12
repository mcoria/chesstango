package net.chesstango.search.smart.alphabeta;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter {

    int maximize(final int currentPly, final int alpha, final int beta);

    int minimize(final int currentPly, final int alpha, final int beta);
}
