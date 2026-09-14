package net.chesstango.search.visitors;

import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.alphabeta.root.filters.AspirationWindows;
import net.chesstango.search.alphabeta.statistics.evalcache.EvaluatorCacheCounters;
import net.chesstango.search.alphabeta.statistics.evaluator.EvaluatorCounters;
import net.chesstango.search.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.alphabeta.statistics.game.GameCountersCollector;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.alphabeta.statistics.sorter.SorterCounters;
import net.chesstango.search.alphabeta.statistics.transposition.TTableCounters;

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
    public void visit(AspirationWindows aspirationWindows) {
        searchResult.setWithAspirationWindows(true);
    }

    @Override
    public void visit(EvaluatorCounters evaluatorCounters) {
        searchResult.setEvaluatorStatistics(evaluatorCounters.getEvaluationStatistics());
    }

    @Override
    public void visit(EvaluatorCacheCounters evaluatorCacheCounters) {
        searchResult.setEvaluatorCacheStatistics(evaluatorCacheCounters.getEvaluatorCacheStatistics());
    }

    @Override
    public void visit(NodeCounters nodeCounters) {
        searchResult.setNodeStatistics(nodeCounters.getNodeStatistics());
    }

    @Override
    public void visit(SorterCounters sorterCounters) {
        searchResult.setSorterStatistics(sorterCounters.getSorterStatistics());
    }

    @Override
    public void visit(GameCountersCollector gameCounters) {
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
    public void visit(DepthCollector depthCollector) {
        searchResult.setExploredDepth(depthCollector.getExploredDepth());
    }

}
