package net.chesstango.search;

import net.chesstango.search.dummy.Dummy;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.core.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.evaluator.filters.LoopEvaluation;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchLast;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.debug.traps.LeafNodeTrap;
import net.chesstango.search.smart.alphabeta.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.evaluator.visitors.SetEvaluatorVisitor;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.pv.TTPVReaderDebug;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.filters.TranspositionPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.quiescence.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.alphabeta.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.statistics.transposition.TTableStatisticsCollector;
import net.chesstango.search.smart.alphabeta.transposition.TTableDebug;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.alphabeta.transposition.listeners.ResetTranspositionTables;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTDump;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TTLoad;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;
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
