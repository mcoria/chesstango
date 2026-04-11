package net.chesstango.search.smart.alphabeta;

import net.chesstango.search.Acceptor;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter extends Acceptor {

    int maximize(final int currentPly, final int alpha, final int beta);

    int minimize(final int currentPly, final int alpha, final int beta);

}
