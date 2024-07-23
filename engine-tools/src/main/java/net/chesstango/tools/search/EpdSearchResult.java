package net.chesstango.tools.search;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EPD epd,
                              SearchMoveResult searchResult,

                              // Mejor movimiento encontrado en notacion algebraica
                              String bestMoveFound,
                              boolean isSearchSuccess,

                              // Exactitud: de la lista de movimientos en profundidad, que movimientos son exitosos
                              int depthAccuracyPct) {

    public String getText() {
        return epd.getText();
    }
}
