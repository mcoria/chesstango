package net.chesstango.search.visitors;

import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;

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
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        searchResultByDepth.setBestRootMoveEvaluation(alphaBetaFacade.getBestMoveEvaluation());
    }


    @Override
    public void visit(PVCalculatorTransposition transpositionPVReader) {
        searchResultByDepth.setPrincipalVariation(transpositionPVReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(transpositionPVReader.isPvComplete());
    }

    @Override
    public void visit(PVCalculatorTriangular trianglePVReader) {
        searchResultByDepth.setPrincipalVariation(trianglePVReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(trianglePVReader.isPvComplete());
    }

    @Override
    public void visit(RootMoveEvaluationCollection moveEvaluations) {
        searchResultByDepth.setRootMoveEvaluations(moveEvaluations.getRootMoveEvaluations());
    }

    @Override
    public void visit(SetSearchTimers setSearchTimers) {
        searchResultByDepth.setTimeSearching(setSearchTimers.getTimeSearching());
        searchResultByDepth.setTimeSearchingLastDepth(setSearchTimers.getTimeSearchingLastDepth());
    }
}
