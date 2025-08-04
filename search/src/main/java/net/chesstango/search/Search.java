package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.builders.AlphaBetaBuilder;

/**
 * 
 * @author Mauricio Coria
 */
public interface Search {

    /**
     * Search up to depth
     */
    SearchResult search(Game game);

    /**
     * Stop searching. This method may be called while another thread is searching
     */
    void stopSearching();

    /**
     * Reset internal counters and buffers (for instance TT)
     */
    void reset();

    /**
     * Set search parameters
     */
    void setSearchParameter(SearchParameter parameter, Object value);


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
