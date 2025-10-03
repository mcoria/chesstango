package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * Thread synchronization must be performed outside this class.
 *
 * @author Mauricio Coria
 */
public interface Search extends Acceptor {

    /**
     * Start searching. Do not call stopSearch() until at least a SearchResultByDepth = 1 has been completed.
     */
    SearchResult startSearch(Game game);

    /**
     * Stop searching. This method may be called while another thread is searching.
     */
    void stopSearch();

    /**
     * Reset internal counters and buffers (for instance TT)
     */
    void reset();

    /**
     * Creates a default Search instance using the default Evaluator.
     *
     * @return a new Search instance configured with default settings
     */
    static SearchBuilder<?> newSearchBuilder() {
        return AlphaBetaBuilder.createDefaultBuilderInstance();
    }

}
