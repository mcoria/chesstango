package net.chesstango.search.smart;

import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public interface SearchAlgorithm {

    /**
     * Invoked once per search depth
     *
     * @return
     */
    SearchResultByDepth search();
}
