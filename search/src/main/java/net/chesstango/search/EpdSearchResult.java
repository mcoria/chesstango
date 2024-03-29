package net.chesstango.search;

import net.chesstango.board.representations.EPDEntry;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EPDEntry epdEntry,
                              SearchMoveResult searchResult,
                              String bestMoveFoundStr,
                              boolean epdResult) {
    public String getText() {
        return epdEntry.text;
    }
}
