package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * Thread synchronization must be performed outside this class.
 *
 * @author Mauricio Coria
 */
public interface Search {

    /**
     * Set search parameters. For instance:
     * - SearchResultByDepthListener
     */
    void setSearchParameter(SearchParameter parameter, Object value);

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
    static Search getInstance() {
        return getInstance(Evaluator.getInstance());
    }

    /**
     * Creates a Search instance with a custom Evaluator.
     *
     * @param evaluator the custom Evaluator to be used for position evaluation
     * @return a new Search instance configured with the specified evaluator
     */
    static Search getInstance(Evaluator evaluator) {
        return AlphaBetaBuilder.createDefaultBuilderInstance(evaluator).build();
    }
}
