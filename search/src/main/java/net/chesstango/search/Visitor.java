package net.chesstango.search;

import net.chesstango.search.dummy.Dummy;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.features.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.features.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.features.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchLast;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchTimers;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.features.debug.traps.LeafNodeTrap;
import net.chesstango.search.smart.features.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.features.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.features.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.evaluator.visitors.SetEvaluatorVisitor;
import net.chesstango.search.smart.features.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.pv.TTPVReaderDebug;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.features.quiescence.Quiescence;
import net.chesstango.search.smart.features.quiescence.QuiescenceNull;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.statistics.transposition.TTableStatisticsCollector;
import net.chesstango.search.smart.features.transposition.TTableDebug;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.features.transposition.listeners.ResetTranspositionTables;
import net.chesstango.search.smart.features.transposition.listeners.TTDump;
import net.chesstango.search.smart.features.transposition.listeners.TTLoad;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

/**
 * @author Mauricio Coria
 */
public interface Visitor {

    default void visit(Dummy dummy) {
    }

    default void visit(IterativeDeepening iterativeDeepening) {
    }

    default void visit(NoIterativeDeepening noIterativeDeepening) {
    }

    /**
     * Facades
     */
    default void visit(AlphaBetaFacade alphaBetaFacade) {
    }

    default void visit(BottomMoveCounterFacade bottomMoveCounterFacade) {
    }

    /**
     * Alpha Beta filters
     *
     */

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

    default void visit(StopProcessingCatch stopProcessingCatch) {
    }

    default void visit(MoveEvaluationTracker moveEvaluationTracker) {
    }

    default void visit(TranspositionPV transpositionPV) {
    }

    default void visit(TriangularPV triangularPV) {
    }

    default void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
    }

    default void visit(TranspositionTableTerminal transpositionTableTerminal) {
    }

    default void visit(LoopEvaluation loopEvaluation) {
    }

    default void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
    }

    default void visit(EgtbEvaluation egtbEvaluation) {
    }

    default void visit(TranspositionTable transpositionTable) {
    }

    default void visit(ExtensionFlowControl extensionFlowControl) {
    }

    default void visit(KillerMoveTracker killerMoveTracker) {
    }

    default void visit(TranspositionTableQ transpositionTableQ) {
    }

    default void visit(Quiescence quiescence) {
    }

    default void visit(QuiescenceNull quiescenceNull) {
    }

    default void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
    }

    default void visit(QuiescenceStatisticsVisited quiescenceStatisticsVisited) {
    }

    default void visit(DebugFilter debugFilter) {
    }

    default void visit(ZobristTracker zobristTracker) {
    }

    default void visit(TTDump ttDump) {
    }

    default void visit(TTLoad ttLoad) {
    }

    /**
     *
     * Setter elements
     */
    default void visit(SetGameToEvaluator setGameToEvaluator) {
    }

    default void visit(SetTrianglePV setTrianglePV) {
    }

    default void visit(SetNodeStatistics setNodeStatistics) {
    }

    default void visit(SetSearchTracker setSearchTracker) {
    }

    default void visit(SetDebugOutput setDebugOutput) {
    }

    default void visit(KillerMovesDebug killerMovesDebug) {
    }

    default void visit(TTPVReader ttpvReader) {
    }

    default void visit(TTPVReaderDebug ttpvReaderDebug) {
    }

    default void visit(TTableDebug tableDebug) {
    }

    default void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
    }


    default void visit(EvaluatorDebug evaluatorDebug) {
    }

    default void visit(SetKillerMoveTables setKillerMoveTables) {
    }

    default void visit(SetKillerMoveTablesDebug setKillerMoveTablesDebug) {
    }

    default void visit(ResetTranspositionTables resetTranspositionTables) {
    }

    default void visit(LeafNodeTrap leafNodeTrap) {
    }

    default void visit(SetSearchLast setSearchLast) {
    }

    default void visit(SetSearchTimers setSearchTimers) {
    }

    default void visit(SetEvaluatorVisitor setEvaluatorVisitor) {
    }

    default void visit(SetGameToEndGameTableBase setGameToEndGameTableBase) {
    }


    /**
     *
     * Sorter elements
     */
    default void visit(RootMoveSorter rootMoveSorter) {
    }

    default void visit(NodeMoveSorter nodeMoveSorter) {
    }

    default void visit(MoveSorterDebug moveSorterDebug) {
    }

    default void visit(EvaluatorStatisticsCollector evaluatorStatisticsCollector) {
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

    default void visit(TranspositionHeadMoveComparatorQ transpositionHeadMoveComparatorQ) {
    }

    default void visit(TranspositionTailMoveComparatorQ transpositionTailMoveComparatorQ) {
    }

    default void visit(QuietComparator quietComparator) {
    }

    default void visit(KillerMoveComparator killerMoveComparator) {
    }

    default void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
    }

    default void visit(PromotionComparator promotionComparator) {
    }

    default void visit(RecaptureMoveComparator recaptureMoveComparator) {
    }

    default void visit(MvvLvaComparator mvvLvaComparator) {
    }


    default void visit(TTableStatisticsCollector tTableStatisticsCollector) {
    }

}
