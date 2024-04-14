package net.chesstango.tools.search;

import net.chesstango.board.representations.EpdEntry;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EpdEntry epdEntry,
                              SearchMoveResult searchResult,
                              String bestMoveFoundStr,
                              boolean isSearchSuccess) {

    public EpdSearchResult(EpdEntry epdEntry,
                           SearchMoveResult searchResult,
                           String bestMoveFoundStr) {
        this(epdEntry, searchResult, bestMoveFoundStr, epdEntry.isMoveSuccess(searchResult.getBestMove()));
    }

    public String getText() {
        return epdEntry.text;
    }
}
