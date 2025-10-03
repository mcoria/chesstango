package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;

import java.util.function.Consumer;

/**
 * Esta clase recorre ella misma toda la estructura
 *
 * @author Mauricio Coria
 */

public class SetSearchByDepthVisitor implements Visitor {


    private final Consumer<SearchResultByDepth> searchResultByDepthListener;

    public SetSearchByDepthVisitor(Consumer<SearchResultByDepth> searchResultByDepthListener) {
        this.searchResultByDepthListener = searchResultByDepthListener;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        iterativeDeepening.setSearchResultByDepthListener(searchResultByDepthListener);
    }

}
