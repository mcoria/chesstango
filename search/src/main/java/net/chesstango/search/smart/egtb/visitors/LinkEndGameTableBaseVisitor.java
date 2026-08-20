package net.chesstango.search.smart.egtb.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.egtb.EndGameTableBase;
import net.chesstango.search.smart.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;

/**
 *
 * @author Mauricio Coria
 */
public class LinkEndGameTableBaseVisitor implements Visitor {
    private final EndGameTableBase endGameTableBase;

    public LinkEndGameTableBaseVisitor(EndGameTableBase endGameTableBase) {
        this.endGameTableBase = endGameTableBase;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchListenerMediator searchListenerMediator = iterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        SearchListenerMediator searchListenerMediator = noIterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        alphaBetaFlowControl.setEndGameTableBase(endGameTableBase);
    }

    @Override
    public void visit(EgtbEvaluation egtbEvaluation) {
        egtbEvaluation.setEndGameTableBase(endGameTableBase);
    }

    @Override
    public void visit(SetGameToEndGameTableBase setGameToEndGameTableBase) {
        setGameToEndGameTableBase.setEndGameTableBase(endGameTableBase);
    }

    @Override
    public void visit(PVCalculatorTriangular setTrianglePV) {
        setTrianglePV.setEndGameTableBase(endGameTableBase);
    }

}
