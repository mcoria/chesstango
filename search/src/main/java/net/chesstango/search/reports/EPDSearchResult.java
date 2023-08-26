package net.chesstango.search.reports;

import net.chesstango.board.representations.EPDEntry;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public class EPDSearchResult {
    public EPDEntry epdEntry;
    public String bestMoveFoundStr;
    public boolean bestMoveFound;
    public long searchDuration;

    public SearchMoveResult searchResult;

    public String getText() {
        return epdEntry.text;
    }
}
