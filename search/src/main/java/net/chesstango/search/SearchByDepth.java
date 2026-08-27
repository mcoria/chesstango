package net.chesstango.search;

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
