package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchTimers;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;

/**
 *
 * @author Mauricio Coria
 */
public class CollectSearchResultVisitor implements Visitor {

    private final SearchResult searchResult;

    public CollectSearchResultVisitor(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    @Override
    public void visit(EvaluatorStatisticsWrapper evaluatorStatisticsWrapper) {
        searchResult.setEvaluationStatistics(evaluatorStatisticsWrapper.getEvaluationStatistics());
    }

    @Override
    public void visit(SetNodeStatistics setNodeStatistics) {
        searchResult.setExecutedMoves(setNodeStatistics.getExecutedMoves());
        searchResult.setRegularNodeStatistics(setNodeStatistics.getRegularNodeStatistics());
        searchResult.setQuiescenceNodeStatistics(setNodeStatistics.getQuiescenceNodeStatistics());
    }

    @Override
    public void visit(SetSearchTimers setSearchTimers) {
        searchResult.setTimeSearching(setSearchTimers.getTimeSearching());
    }

    @Override
    public void visit(BottomMoveCounterFacade bottomMoveCounterFacade) {
        searchResult.setBottomMoveCounter(bottomMoveCounterFacade.getBottomMoveCounter());
    }

}
