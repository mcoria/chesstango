package net.chesstango.search.reports;

import net.chesstango.board.representations.EPDEntry;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public record EPDSearchResult(EPDEntry epdEntry,
                              String bestMoveFoundStr,
                              boolean bestMoveFound,
                              long searchDuration,
                              SearchMoveResult searchResult) {
    public String getText() {
        return epdEntry.text;
    }
}
