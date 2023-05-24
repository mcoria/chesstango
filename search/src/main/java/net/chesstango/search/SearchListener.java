package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void searchStarted();

    void searchInfo(int depth, int selDepth, List<Move> pv);

    void searchStopped();

    void searchFinished(SearchMoveResult searchResult);
}
