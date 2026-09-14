package net.chesstango.search.alphabeta.egtb.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.IterativeDeepening;
import net.chesstango.search.NoIterativeDeepening;
import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.alphabeta.egtb.filters.EgtbEvaluation;
import net.chesstango.search.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.alphabeta.pv.model.PVCalculator;

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
        ListenerMediator listenerMediator = iterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
    }

    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        ListenerMediator listenerMediator = noIterativeDeepening.getListenerMediator();
        listenerMediator.accept(this);
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
    public void visit(PVCalculator setTrianglePV) {
        setTrianglePV.setEndGameTableBase(endGameTableBase);
    }

}
