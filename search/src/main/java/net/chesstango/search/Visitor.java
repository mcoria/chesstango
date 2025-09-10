package net.chesstango.search;

import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

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

    default void visit(KillerMoveTracker killerMoveTracker) {
    }

    default void visit(TranspositionTableQ transpositionTableQ) {
    }

    default void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
    }

    default void visit(Quiescence quiescence) {
    }

    default void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
    }

    /**
     *
     * Sorter elements
     */
    default void visit(RootMoveSorter rootMoveSorter) {
    }

    default void visit(NodeMoveSorter nodeMoveSorter) {
    }

    /**
     *
     * Comparator elements
     */
    default void visit(DefaultMoveComparator defaultMoveComparator) {
    }

    default void visit(PrincipalVariationComparator principalVariationComparator) {
    }

    default void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
    }

    default void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
    }

    default void visit(QuietComparator quietComparator) {
    }

    default void visit(KillerMoveComparator killerMoveComparator) {
    }

    default void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
    }

    default void visit(PromotionComparator promotionComparator){}

    default void visit(RecaptureMoveComparator recaptureMoveComparator){}

    default void visit(MvvLvaComparator mvvLvaComparator){}
}
