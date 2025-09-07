package net.chesstango.search.visitors;

import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;

/**
 * @author Mauricio Coria
 */
public class VisitorIterator implements Visitor {
    private final Visitor visitor;

    public VisitorIterator(Visitor visitor) {
        this.visitor = visitor;
    }

    public void iterate(Acceptor aceptor) {
        aceptor.accept(this);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        visitor.visit(iterativeDeepening);

        SearchAlgorithm algorithm = iterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);
    }

    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        visitor.visit(alphaBetaFacade);

        AlphaBetaFilter alphaBeta = alphaBetaFacade.getAlphaBetaFilter();
        alphaBeta.accept(this);
    }

    @Override
    public void visit(AspirationWindows aspirationWindows) {
        visitor.visit(aspirationWindows);
        aspirationWindows.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        visitor.visit(transpositionTableRoot);
        transpositionTableRoot.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        visitor.visit(alphaBetaStatisticsExpected);
        alphaBetaStatisticsExpected.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBeta alphaBeta) {
        visitor.visit(alphaBeta);
        alphaBeta.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
        visitor.visit(alphaBetaStatisticsVisited);
        alphaBetaStatisticsVisited.getNext().accept(this);
    }

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
        visitor.visit(moveEvaluationTracker);
        moveEvaluationTracker.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionPV transpositionPV) {
        visitor.visit(transpositionPV);
        transpositionPV.getNext().accept(this);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        visitor.visit(alphaBetaFlowControl);
    }
}
