package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;

/**
 * Represents a chess search algorithm that finds the best move for a given position.
 * <p>
 * This interface provides the core functionality for searching through chess positions
 * using various algorithms (typically alpha-beta pruning with enhancements). The search
 * can be configured with different features such as transposition tables, iterative deepening,
 * move ordering, and evaluation functions.
 * </p>
 * <p>
 * <b>Thread Safety:</b> Implementations of this interface are NOT thread-safe. Thread synchronization
 * must be performed by the caller. Only one thread should call {@link #startSearch(Game)} at a time.
 * However, {@link #stopSearch()} may be called from a different thread while a search is in progress.
 * </p>
 * <p>
 * <b>Typical Usage:</b>
 * </p>
 * <pre>{@code
 * Search search = Search.newSearchBuilder()
 *     .withGameEvaluator(new EvaluatorByMaterial())
 *     .withTranspositionTable()
 *     .withStatistics()
 *     .build();
 *
 * search.accept(new SetMaxDepthVisitor(5));
 * SearchResult result = search.startSearch(game);
 * Move bestMove = result.getBestMove();
 * }</pre>
 *
 * @author Mauricio Coria
 * @see SearchResult
 * @see SearchBuilder
 * @see net.chesstango.search.visitors.SetMaxDepthVisitor
 * @see net.chesstango.search.visitors.SetDepthVisitor
 */
public interface Search extends Acceptor {

    /**
     * Starts searching for the best move in the given game position.
     * <p>
     * This method performs a search using the configured algorithm (typically iterative deepening
     * with alpha-beta pruning) to find the best move. The search depth and other parameters should
     * be configured using visitors (e.g., {@link net.chesstango.search.visitors.SetMaxDepthVisitor})
     * before calling this method.
     * </p>
     * <p>
     * <b>Important:</b> Do not call {@link #stopSearch()} until at least a search result for depth 1
     * has been completed. Calling stopSearch() too early may result in no valid move being returned.
     * </p>
     * <p>
     * <b>Thread Safety:</b> This method is NOT thread-safe and should only be called by one thread
     * at a time. However, {@link #stopSearch()} may be called from another thread to interrupt the search.
     * </p>
     *
     * @param game the current game state from which to search for the best move; must not be null
     * @return a SearchResult containing the best move found, evaluation score, principal variation,
     * and optionally statistics about the search
     * @throws NullPointerException if game is null
     * @see #stopSearch()
     * @see SearchResult
     */
    SearchResult startSearch(Game game);

    /**
     * Requests the search to stop as soon as possible.
     * <p>
     * This method signals the search algorithm to terminate gracefully. The search will complete
     * the current iteration or node evaluation and return the best move found so far. This method
     * is typically called from a different thread (e.g., a timer thread or UI thread) while
     * {@link #startSearch(Game)} is executing in another thread.
     * </p>
     * <p>
     * <b>Thread Safety:</b> This method is designed to be called from a different thread than the one
     * executing {@link #startSearch(Game)}. It is safe to call this method at any time after the search
     * has started, but calling it before a depth-1 search completes may result in no valid move being available.
     * </p>
     * <p>
     * <b>Behavior:</b> The method returns immediately; it does not wait for the search to actually stop.
     * The search will stop asynchronously, and {@link #startSearch(Game)} will return when ready.
     * </p>
     *
     * @see #startSearch(Game)
     */
    void stopSearch();

    /**
     * Resets all internal state, counters, and caches to their initial values.
     * <p>
     * This method clears transient search data such as:
     * </p>
     * <ul>
     *   <li>Transposition table entries</li>
     *   <li>Search statistics (node counts, cut-offs, etc.)</li>
     *   <li>History heuristic tables</li>
     *   <li>Killer move tables</li>
     *   <li>Any other cached search data</li>
     * </ul>
     * <p>
     * <b>When to call:</b> This method should be called when starting a new game or when you want
     * to ensure that data from previous searches does not influence future searches. For example,
     * call this method before starting a tournament game or when switching between different positions
     * that are not related.
     * </p>
     * <p>
     * <b>Note:</b> This method does NOT reset the search configuration (evaluator, max depth, etc.),
     * only the dynamic state accumulated during searches.
     * </p>
     *
     * @see TranspositionTable
     */
    void reset();

    /**
     * Creates a default Search instance using the default Evaluator.
     * <p>
     * This factory method returns a builder that can be used to configure and construct a Search instance.
     * The default builder is configured with:
     * </p>
     * <ul>
     *   <li>Alpha-beta pruning algorithm</li>
     *   <li>Default evaluation function (material-based)</li>
     *   <li>Standard move ordering</li>
     * </ul>
     * <p>
     * Additional features such as transposition tables, iterative deepening, statistics collection,
     * and aspiration windows can be added by calling methods on the returned builder before calling
     * {@code build()}.
     * </p>
     * <p>
     * <b>Example:</b>
     * </p>
     * <pre>{@code
     * Search search = Search.newSearchBuilder()
     *     .withGameEvaluator(new EvaluatorByMaterial())
     *     .withTranspositionTable()
     *     .withStatistics()
     *     .build();
     * }</pre>
     *
     * @return a new SearchBuilder instance configured with default settings
     * @see SearchBuilder
     * @see net.chesstango.search.builders.AlphaBetaBuilder
     */
    static SearchBuilder<?> newSearchBuilder() {
        return AlphaBetaBuilder.createDefaultBuilderInstance();
    }

}
