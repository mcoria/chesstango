package net.chesstango.search;

/**
 * A runtime exception used to immediately terminate an ongoing search operation in the chess engine.
 *
 * <p>This exception is thrown when a search needs to be stopped prematurely, typically due to:
 * <ul>
 *   <li>Time constraints being exceeded</li>
 *   <li>External interruption requests</li>
 *   <li>Search depth or node limits being reached</li>
 *   <li>User-initiated cancellation</li>
 * </ul>
 *
 * <p>As a RuntimeException, it can be thrown from any point in the search tree without requiring
 * explicit declaration in method signatures, allowing for clean propagation up the call stack to
 * the search entry point where it can be caught and handled appropriately.
 *
 * <p>This exception is typically caught at the top level of the search algorithm to ensure that
 * partial search results can be retrieved and the search can be gracefully terminated.
 *
 * @author Mauricio Coria
 * @see net.chesstango.search.Search
 * @see net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl
 */
public class StopSearchingException extends RuntimeException {
}
