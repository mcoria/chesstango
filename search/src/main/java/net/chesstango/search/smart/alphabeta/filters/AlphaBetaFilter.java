package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.search.smart.SearchListener;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter extends SearchListener {

    long maximize(final int currentPly, final int alpha, final int beta);

    long minimize(final int currentPly, final int alpha, final int beta);

}
