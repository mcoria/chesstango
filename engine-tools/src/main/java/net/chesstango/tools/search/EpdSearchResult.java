package net.chesstango.tools.search;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.search.SearchResult;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EPD epd,
                              SearchResult searchResult,

                              // Mejor movimiento encontrado en notacion algebraica
                              String bestMoveFound,
                              boolean isSearchSuccess,

                              // Exactitud: de la lista de movimientos en profundidad, que movimientos son exitosos
                              int depthAccuracyPct) {

    public String getText() {
        return epd.getText();
    }
}
