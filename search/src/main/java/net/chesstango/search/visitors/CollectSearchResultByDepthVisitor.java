package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.root.RootMoveEvaluationBest;
import net.chesstango.search.smart.root.RootMoveEvaluationCache;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;

/**
 *
 * @author Mauricio Coria
 */
public class CollectSearchResultByDepthVisitor implements Visitor {

    private final SearchResultByDepth searchResultByDepth;

    public CollectSearchResultByDepthVisitor(SearchResultByDepth searchResultByDepth) {
        this.searchResultByDepth = searchResultByDepth;
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        searchResultByDepth.setSearchStopped(stopProcessingCatch.isSearchStopped());
    }

    @Override
    public void visit(PVCalculatorTriangular trianglePVReader) {
        searchResultByDepth.setPrincipalVariation(trianglePVReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(trianglePVReader.isPvComplete());
    }

    @Override
    public void visit(RootMoveEvaluationCache rootMoveEvaluationCache) {
        searchResultByDepth.setRootMoveEvaluations(rootMoveEvaluationCache.getRootMoveEvaluations());
    }

    @Override
    public void visit(RootMoveEvaluationBest rootMoveEvaluationBest) {
        searchResultByDepth.setBestRootMoveEvaluation(rootMoveEvaluationBest.getBestRootMoveEvaluation());
    }

    @Override
    public void visit(SetSearchTimers setSearchTimers) {
        searchResultByDepth.setTimeSearching(setSearchTimers.getTimeSearching());
        searchResultByDepth.setTimeSearchingLastDepth(setSearchTimers.getTimeSearchingLastDepth());
    }


    @Override
    public void visit(DebugNodeTracker debugNodeTracker) {
        searchResultByDepth.setDebugNodes(debugNodeTracker.getDebugNodes());
    }
}
