package net.chesstango.search.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.RootMoveEvaluation;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.pv.listeners.SetTrianglePV;

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
    public void visit(TTPVReader ttpvReader) {
        searchResultByDepth.setPrincipalVariation(ttpvReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(ttpvReader.isPvComplete());
    }

    @Override
    public void visit(SetTrianglePV setTrianglePV) {
        setTrianglePV.calculatePrincipalVariation(searchResultByDepth.getBestRootMoveEvaluation());
        searchResultByDepth.setPrincipalVariation(setTrianglePV.getPrincipalVariation());
        searchResultByDepth.setPvComplete(setTrianglePV.isPvComplete());
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
