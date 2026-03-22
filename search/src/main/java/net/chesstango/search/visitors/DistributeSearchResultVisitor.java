package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;

/**
 *
 * @author Mauricio Coria
 */
public class DistributeSearchResultVisitor implements Visitor {
    private final SearchResult searchResult;

    public DistributeSearchResultVisitor(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

}
