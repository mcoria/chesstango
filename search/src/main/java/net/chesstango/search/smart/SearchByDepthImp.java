package net.chesstango.search.smart;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.*;
import net.chesstango.search.smart.root.RootMoveEvaluationBest;
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

            int value = next.alphaBeta(0, Evaluator.INFINITE_NEGATIVE, Evaluator.INFINITE_POSITIVE);

            RootMoveEvaluation bestRootMoveEvaluation = rootMoveEvaluationBest.getBestRootMoveEvaluation();

            if (bestRootMoveEvaluation.evaluation() != value) {
                throw new RuntimeException("Best root move evaluation value is not the same as the value returned by the search algorithm");
            }

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
