package net.chesstango.tools.search;

import net.chesstango.board.representations.epd.EPD;
import net.chesstango.search.SearchResult;

import java.util.function.BiFunction;

/**
 * @author Mauricio Coria
 */
public interface EpdSearchResultBuilder extends BiFunction<EPD, SearchResult, EpdSearchResult> {
}
