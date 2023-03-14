package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaSearch {
    int maximize(final Game game, final int currentPly, final int alpha, final int beta, final SearchContext context);

    int minimize(final Game game, final int currentPly, final int alpha, final int beta, final SearchContext context);

    void stopSearching();
}
