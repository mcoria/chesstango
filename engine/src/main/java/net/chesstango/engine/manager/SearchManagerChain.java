package net.chesstango.engine.manager;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public sealed interface SearchManagerChain permits SearchManagerByAlgorithm, SearchManagerByBook {
    void reset();

    void stopSearching();

    void open();

    void close();

    SearchMoveResult searchImp(Game game, int depth);
}
