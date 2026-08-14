package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public interface DebugIteratorHandler {
    void startIteration();

    void endIteration();

    void startDepth(int depth);

    void endDepth();

    void startWindows();

    void endWindows();

    void startRootNode();

    void endRootNode();

    void startChildNodes();

    void endChildNodes();

    void startRegularNode();

    void endRegularNode();

    void visit(SearchResult searchResult);

    void visit(SearchResultByDepth searchResultByDepth);

    void visit(DebugNode node);
}
