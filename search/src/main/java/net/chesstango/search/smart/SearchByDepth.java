package net.chesstango.search.smart;

import net.chesstango.search.SearchResultByDepth;

/**
 * @author Mauricio Coria
 */
public interface SearchByDepth {

    /**
     * Invoked once per search depth
     *
     * @return
     */
    SearchResultByDepth search(int depth);
}
