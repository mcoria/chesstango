package net.chesstango.search.visitors;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchTimers;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;

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
        MoveEvaluation bestMoveEvaluation = alphaBetaFacade.getBestMoveEvaluation();

        searchResultByDepth.setBestMoveEvaluation(bestMoveEvaluation);

        /**
         * Aca hay un issue; si PV.depth > currentSearchDepth quiere decir que es un mate encontrado más alla del horizonte
         * Deberiamos continuar buscando hasta que se encuentre un mate antes del horizonte
         */
        searchResultByDepth.setContinueDeepening(
                Evaluator.WHITE_WON != bestMoveEvaluation.evaluation() &&
                        Evaluator.BLACK_WON != bestMoveEvaluation.evaluation()
        );
    }


    @Override
    public void visit(TTPVReader ttpvReader) {
        searchResultByDepth.setPrincipalVariation(ttpvReader.getPrincipalVariation());
        searchResultByDepth.setPvComplete(ttpvReader.isPvComplete());
    }

    @Override
    public void visit(SetTrianglePV setTrianglePV) {
        setTrianglePV.calculatePrincipalVariation(searchResultByDepth.getBestMoveEvaluation());
        searchResultByDepth.setPrincipalVariation(setTrianglePV.getPrincipalVariation());
        searchResultByDepth.setPvComplete(setTrianglePV.isPvComplete());
    }

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
        searchResultByDepth.setMoveEvaluations(moveEvaluationTracker.getCurrentMoveEvaluations());
    }

    @Override
    public void visit(SetSearchTimers setSearchTimers) {
        searchResultByDepth.setTimeSearching(setSearchTimers.getTimeSearching());
        searchResultByDepth.setTimeSearchingLastDepth(setSearchTimers.getTimeSearchingLastDepth());
    }

}
