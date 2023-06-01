package net.chesstango.search;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface SearchListener {
    void searchStarted();

    void searchInfo(SearchInfo info);

    void searchStopped();

    void searchFinished(SearchMoveResult searchResult);
}
