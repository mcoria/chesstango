package net.chesstango.search.alphabeta;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.visitors.CollectSearchResultByDepthVisitor;
import net.chesstango.search.visitors.DistributeSearchResultByDepthVisitor;
import net.chesstango.search.visitors.SetDepthVisitor;

/**
 * @author Mauricio Coria
 */
public class SearchByDepthImp implements SearchByDepth, Acceptor {

    @Setter
    @Getter
    private AlphaBetaFilter next;

    @Setter
    private RootMoveEvaluationBest rootMoveEvaluationBest;

    @Setter
    private ListenerMediator listenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    @Override
    public SearchResultByDepth search(int depth) {
        try {
            listenerMediator.accept(new SetDepthVisitor(depth));

            listenerMediator.triggerBeforeSearchByDepth();

            next.alphaBeta(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

            listenerMediator.triggerAfterSearchByDepth(false);
        } catch (StopSearchingException stopSearchingException) {
            listenerMediator.triggerAfterSearchByDepth(true);

            if (rootMoveEvaluationBest.getBestRootMoveEvaluation() == null) {
                return null;
            }
        }

        // Prepare search result
        SearchResultByDepth searchResultByDepth = new SearchResultByDepth(depth);

        listenerMediator.accept(new CollectSearchResultByDepthVisitor(searchResultByDepth));

        listenerMediator.accept(new DistributeSearchResultByDepthVisitor(searchResultByDepth));

        return searchResultByDepth;
    }
}
