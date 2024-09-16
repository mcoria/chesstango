package net.chesstango.tools.search;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.representations.epd.EPD;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
@Accessors(chain = true)
@Getter
@Setter
public class EpdSearchResult {
    private final EPD epd;

    private final SearchResult searchResult;

    boolean isSearchSuccess;

    private String bestMoveFound;

    // Exactitud: de la lista de movimientos en profundidad, que movimientos son exitosos
    private int depthAccuracyPct;

    public EpdSearchResult(EPD epd, SearchResult searchResult) {
        this.epd = epd;
        this.searchResult = searchResult;
    }

    public String getText() {
        return epd.getText();
    }

    public int getBottomMoveCounter() {
        return searchResult.getBottomMoveCounter();
    }
}
