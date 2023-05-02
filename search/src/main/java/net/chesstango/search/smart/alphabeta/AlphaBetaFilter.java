package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter extends FilterActions{

    long maximize(final int currentPly, final int alpha, final int beta);

    long minimize(final int currentPly, final int alpha, final int beta);

}
