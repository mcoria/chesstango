package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;

/**
 * @author Mauricio Coria
 */
public interface Visitor {

    default void visit(IterativeDeepening iterativeDeepening) {
    }

    default void visit(AlphaBetaFacade alphaBetaFacade) {
    }

    default void visit(AspirationWindows aspirationWindows) {
    }

    default void visit(TranspositionTableRoot transpositionTableRoot) {
    }

    default void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
    }

    default void visit(AlphaBeta alphaBeta) {
    }

    default void visit(AlphaBetaStatisticsVisited alphaBetaStatisticsVisited) {
    }

    default void visit(MoveEvaluationTracker moveEvaluationTracker) {
    }

    default void visit(TranspositionPV transpositionPV) {
    }

    default void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
    }

    default void visit(TranspositionTableTerminal transpositionTableTerminal) {
    }

    default void visit(LoopEvaluation loopEvaluation) {
    }

    default void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
    }

    default void visit(TranspositionTable transpositionTable) {
    }

    default void visit(ExtensionFlowControl extensionFlowControl) {
    }

    default void visit(KillerMoveTracker killerMoveTracker){}
}
