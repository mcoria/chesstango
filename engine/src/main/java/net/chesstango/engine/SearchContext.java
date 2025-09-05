package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.function.Consumer;
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

    private Consumer<SearchResultByDepth> searchResultByDepthListener;

    private int depth;
}
