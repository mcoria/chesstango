package net.chesstango.search.smart.alphabeta.debug;

import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;

/**
 * @author Mauricio Coria
 */
public interface DebugIteratorHandler {
    default void startIteration() {
    }

    default void endIteration() {
    }

    default void startDepth(int depth) {
    }

    default void endDepth() {
    }

    default void startWindows(int alphaBound, int betaBound, int windowsCycle) {
    }

    default void endWindows() {
    }

    default void startRootNode() {
    }

    default void endRootNode() {
    }

    default void startChildNodes() {
    }

    default void endChildNodes() {
    }

    default void startRegularNode() {
    }

    default void endRegularNode() {
    }

    default void visit(SearchResult searchResult) {
    }

    default void visit(SearchResultByDepth searchResultByDepth) {
    }

    default void visit(DebugNode node) {
    }
}
