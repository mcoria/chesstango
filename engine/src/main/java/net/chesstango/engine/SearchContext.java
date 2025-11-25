package net.chesstango.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.chesstango.board.Game;
import net.chesstango.search.SearchResultByDepth;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Holds the context information for a chess game search operation.
 * This class encapsulates all necessary parameters and callbacks needed during the search process.
 *
 * @author Mauricio Coria
 */
@Setter
@Getter
@Accessors(chain = true)
class SearchContext {

    /**
     * The chess game instance representing the current game state to be analyzed.
     */
    private Game game;

    /**
     * The maximum depth to search in the game tree.
     */
    private int depth;

    /**
     * Predicate used to determine whether to continue searching based on the current search results.
     */
    private Predicate<SearchResultByDepth> searchResultByDepthPredicate;

    /**
     * Consumer that processes the search results at each depth level during the search operation.
     */
    private Consumer<SearchResultByDepth> searchResultByDepthConsumer;
}
