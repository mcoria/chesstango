package net.chesstango.tools.search;

import net.chesstango.board.representations.epd.EpdEntry;
import net.chesstango.search.SearchByDepthResult;
import net.chesstango.search.SearchMoveResult;

/**
 * @author Mauricio Coria
 */
public record EpdSearchResult(EpdEntry epdEntry,
                              SearchMoveResult searchResult,

                              // Mejor movimiento encontrado en notacion algebraica
                              String bestMoveFoundAlgNot,
                              boolean isSearchSuccess,

                              // Exactitud: de la lista de movimientos en profundidad, que movimientos sin exitosos
                              int depthAccuracyPct) {

    public EpdSearchResult(EpdEntry epdEntry,
                           SearchMoveResult searchResult,
                           String bestMoveFoundStr) {
        this(epdEntry,
                searchResult,
                bestMoveFoundStr,
                epdEntry.isMoveSuccess(searchResult.getBestMove()),
                epdEntry.calculateAccuracyPct(searchResult.getSearchByDepthResultList()
                        .stream()
                        .map(SearchByDepthResult::getBestMove)
                        .toList())
        );
    }

    public String getText() {
        return epdEntry.text;
    }
}
