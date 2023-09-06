package net.chesstango.search;

import net.chesstango.board.representations.EPDEntry;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EPDEntry epdEntry,
                              String bestMoveFoundStr,
                              boolean bestMoveFound,
                              long searchDuration,
                              SearchMoveResult searchResult) {
    public String getText() {
        return epdEntry.text;
    }
}
