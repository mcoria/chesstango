package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.game.GameCounters;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableCounters;

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
    public void visit(NodeCounters nodeCounters) {
        searchResult.setRegularNodeStatistics(nodeCounters.getRegularNodeStatistics());
        searchResult.setQuiescenceNodeStatistics(nodeCounters.getQuiescenceNodeStatistics());
    }

    @Override
    public void visit(GameCounters gameCounters) {
        searchResult.setExecutedMoves(gameCounters.getExecutedMoves());
    }

    @Override
    public void visit(TTableCounters TTableCounters) {
        searchResult.setTTableStatistics(TTableCounters.getTTableStatistics());
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
