package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.search.smart.SearchContext;

/**
 * @author Mauricio Coria
 */
public interface AlphaBetaFilter {

    void init(Game game, final SearchContext context);

    long maximize(final Game game, final int currentPly, final int alpha, final int beta);

    long minimize(final Game game, final int currentPly, final int alpha, final int beta);

    void stopSearching();
}
