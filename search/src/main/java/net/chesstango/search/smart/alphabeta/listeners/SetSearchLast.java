package net.chesstango.search.smart.alphabeta.listeners;

import lombok.Setter;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SearchByDepthListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.visitors.SetSearchLastNullVisitor;
import net.chesstango.search.visitors.SetSearchLastVisitor;

/**
 * @author Mauricio Coria
 */
public class SetSearchLast implements SearchByCycleListener, SearchByDepthListener, Acceptor {
    private SearchResultByDepth lastSearchResultByDepth;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.lastSearchResultByDepth = null;
    }

    @Override
    public void beforeSearchByDepth(SearchByDepthContext context) {
        if (lastSearchResultByDepth != null) {
            searchListenerMediator.accept(new SetSearchLastVisitor(
                    lastSearchResultByDepth.getBestMoveEvaluation(),
                    lastSearchResultByDepth.getMoveEvaluations(),
                    lastSearchResultByDepth.getPrincipalVariation())
            );
        } else {
            searchListenerMediator.accept(new SetSearchLastNullVisitor());
        }
    }

    @Override
    public void afterSearchByDepth(SearchResultByDepth searchResultByDepth) {
        lastSearchResultByDepth = searchResultByDepth;
    }
}
