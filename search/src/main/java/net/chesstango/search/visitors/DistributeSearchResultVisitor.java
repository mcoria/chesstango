package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.iterators.PrintHtmlDebugHandler;

/**
 *
 * @author Mauricio Coria
 */
public class DistributeSearchResultVisitor implements Visitor {
    private final SearchResult searchResult;

    public DistributeSearchResultVisitor(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public void visit(PrintHtmlDebugHandler printHtmlDebugHandler) {
        printHtmlDebugHandler.searchCompleted(searchResult);
    }
}
