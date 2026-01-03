package net.chesstango.search.smart.features.egtb.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.features.egtb.EndGameTableBase;
import net.chesstango.search.smart.features.egtb.filters.EgtbEvaluation;

/**
 *
 * @author Mauricio Coria
 */
public class SetEndGameTableBaseVisitor implements Visitor {
    private final EndGameTableBase endGameTableBase;

    public SetEndGameTableBaseVisitor(EndGameTableBase endGameTableBase) {
        this.endGameTableBase = endGameTableBase;
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchListenerMediator searchListenerMediator = iterativeDeepening.getSearchListenerMediator();
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
    public void visit(SetGameEvaluator setGameEvaluator) {
        setGameEvaluator.setEndGameTableBase(endGameTableBase);
    }
}
