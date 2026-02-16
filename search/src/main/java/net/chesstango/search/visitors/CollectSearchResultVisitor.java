package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchTimers;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.statistics.transposition.TTableStatisticsCollector;

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
    public void visit(EvaluatorStatisticsCollector evaluatorStatisticsCollector) {
        searchResult.setEvaluationStatistics(evaluatorStatisticsCollector.getEvaluationStatistics());
    }

    @Override
    public void visit(SetNodeStatistics setNodeStatistics) {
        searchResult.setExecutedMoves(setNodeStatistics.getExecutedMoves());
        searchResult.setRegularNodeStatistics(setNodeStatistics.getRegularNodeStatistics());
        searchResult.setQuiescenceNodeStatistics(setNodeStatistics.getQuiescenceNodeStatistics());
    }

    @Override
    public void visit(TTableStatisticsCollector tTableStatisticsCollector) {
        searchResult.setTTableStatistics(tTableStatisticsCollector.getTTableStatistics());
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
