package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter {

    void init(final SearchContext context);

    int maximize(final Game game, final int currentPly, final int alpha, final int beta);

    int minimize(final Game game, final int currentPly, final int alpha, final int beta);

    void stopSearching();
}
