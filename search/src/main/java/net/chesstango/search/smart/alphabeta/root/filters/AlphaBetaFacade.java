package net.chesstango.search.smart.alphabeta.root.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.StopSearchingException;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.visitors.CollectSearchResultByDepthVisitor;
import net.chesstango.search.visitors.DistributeSearchResultByDepthVisitor;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFacade implements SearchAlgorithm, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private RootMoveEvaluationBest rootMoveEvaluationBest;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Setter
    protected int depth;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public SearchResultByDepth search() {
        try {
            searchListenerMediator.triggerBeforeSearchByDepth();

            next.alphaBeta(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

            searchListenerMediator.triggerAfterSearchByDepth(false);
        } catch (StopSearchingException stopSearchingException) {
            searchListenerMediator.triggerAfterSearchByDepth(true);

            if (rootMoveEvaluationBest.getBestRootMoveEvaluation() == null) {
                return null;
            }
        }

        // Prepare search result
        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(depth);

        searchListenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

        searchListenerMediator.accept(new DistributeSearchResultByDepthVisitor(searchResultByDepth));

        return searchResultByDepth;
    }
}
