package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResultByDepthListener;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
@Setter
@Getter
@Accessors(chain = true)
class SearchContext {

    private Game game;

    private Predicate<SearchResultByDepth> searchPredicate;

    private SearchResultByDepthListener searchResultByDepthListener;

    private int depth;
}
