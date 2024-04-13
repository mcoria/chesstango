package net.chesstango.tools.search;

import net.chesstango.board.representations.EPDEntry;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EPDEntry epdEntry,
                              SearchMoveResult searchResult,
                              String bestMoveFoundStr,
                              boolean isSearchSuccess) {
    public String getText() {
        return epdEntry.text;
    }
}
