package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;

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
    public void visit(PVCalculatorTriangular trianglePVReader) {
        searchResultByDepth.setPrincipalVariation(trianglePVReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(trianglePVReader.isPvComplete());
    }

    @Override
    public void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        searchResultByDepth.setBestRootMoveEvaluation(rootMoveEvaluationCollection.getBestRootMoveEvaluation());
        searchResultByDepth.setRootMoveEvaluations(rootMoveEvaluationCollection.getRootMoveEvaluations());
    }

    @Override
    public void visit(SetSearchTimers setSearchTimers) {
        searchResultByDepth.setTimeSearching(setSearchTimers.getTimeSearching());
        searchResultByDepth.setTimeSearchingLastDepth(setSearchTimers.getTimeSearchingLastDepth());
    }
}
