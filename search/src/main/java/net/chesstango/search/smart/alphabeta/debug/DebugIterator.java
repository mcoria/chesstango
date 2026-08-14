package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public class DebugIterator {
    private final SearchResult searchResult;

    public DebugIterator(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public void iterate(DebugIteratorHandler debugIteratorHandler) {
        debugIteratorHandler.startIteration();

        debugIteratorHandler.visit(searchResult);

        int depth = 1;
        for (SearchResultByDepth searchResultByDepth : searchResult.getSearchResultByDepths()) {
            debugIteratorHandler.startDepth(depth);
            iterateResultByDepth(debugIteratorHandler, searchResultByDepth, searchResult.isWithAspirationWindows());
            debugIteratorHandler.endDepth();
            depth++;
        }

        debugIteratorHandler.endIteration();
    }

    void iterateResultByDepth(DebugIteratorHandler debugIteratorHandler,
                              SearchResultByDepth searchResultByDepth,
                              boolean withAspirationWindows) {

        debugIteratorHandler.visit(searchResultByDepth);

        for (DebugNode node : searchResultByDepth.getDebugNodes()) {
            if (withAspirationWindows) {
                debugIteratorHandler.startWindows();
            }

            debugIteratorHandler.startRootNode();

            iterateDebugNode(debugIteratorHandler, node);

            debugIteratorHandler.endRootNode();

            if (withAspirationWindows) {
                debugIteratorHandler.endWindows();
            }
        }
    }

    void iterateDebugNode(DebugIteratorHandler debugIteratorHandler, DebugNode node) {
        debugIteratorHandler.visit(node);
        if (!node.getChildNodes().isEmpty()) {
            debugIteratorHandler.startChildNodes();
            for (DebugNode childNode : node.getChildNodes()) {
                debugIteratorHandler.startRegularNode();
                iterateDebugNode(debugIteratorHandler, childNode);
                debugIteratorHandler.endRegularNode();
            }
            debugIteratorHandler.endChildNodes();
        }
    }
}
