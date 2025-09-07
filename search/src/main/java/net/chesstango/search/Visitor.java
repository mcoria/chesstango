package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
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
public interface Visitor {

    void visit(IterativeDeepening iterativeDeepening);

    void visit(AlphaBetaFacade alphaBetaFacade);

    void visit(AspirationWindows aspirationWindows);

    void visit(TranspositionTableRoot transpositionTableRoot);

    void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected);

    void visit(AlphaBeta alphaBeta);

    void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited);

    void visit(MoveEvaluationTracker moveEvaluationTracker);

    void visit(TranspositionPV transpositionPV);

    void visit(AlphaBetaFlowControl alphaBetaFlowControl);
}
