package net.chesstango.search;

import net.chesstango.search.dummy.Dummy;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.iterators.PrintHtmlDebugHandler;
import net.chesstango.search.smart.debug.listeners.PrintTxtDebugListener;
import net.chesstango.search.smart.debug.traps.LeafNodeTrap;
import net.chesstango.search.smart.egtb.filters.EgtbEvaluation;
import net.chesstango.search.smart.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.evaluator.filters.LoopEvaluation;
import net.chesstango.search.smart.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.evaluator.visitors.LinkEvaluatorVisitor;
import net.chesstango.search.smart.killermoves.KillerMovesDebug;
import net.chesstango.search.smart.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.pv.filters.CalculatePV;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.quiescence.QuiescenceNull;
import net.chesstango.search.smart.root.RootMoveEvaluationBest;
import net.chesstango.search.smart.root.RootMoveEvaluationCache;
import net.chesstango.search.smart.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.root.filters.AlphaBetaFacade;
import net.chesstango.search.smart.root.filters.AspirationWindows;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.statistics.evaluation.EvaluationCounters;
import net.chesstango.search.smart.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.statistics.evaluation.listeners.EvaluatorCacheListener;
import net.chesstango.search.smart.statistics.game.DepthCollector;
import net.chesstango.search.smart.statistics.game.GameCountersCollector;
import net.chesstango.search.smart.statistics.node.NodeCounters;
import net.chesstango.search.smart.statistics.node.filters.*;
import net.chesstango.search.smart.statistics.transposition.*;
import net.chesstango.search.smart.transposition.TTableArrayPrimitives;
import net.chesstango.search.smart.transposition.TTableComparatorDebug;
import net.chesstango.search.smart.transposition.TTableNodeDebug;
import net.chesstango.search.smart.transposition.TTablePVDebug;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.transposition.filters.*;
import net.chesstango.search.smart.transposition.listeners.TTDump;
import net.chesstango.search.smart.transposition.listeners.TTListener;
import net.chesstango.search.smart.transposition.listeners.TTLoad;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;
import net.chesstango.search.sorters.MoveSorterDebug;
import net.chesstango.search.sorters.NodeGroupSorter;
import net.chesstango.search.sorters.NodeMoveSorter;
import net.chesstango.search.sorters.RootMoveSorter;
import net.chesstango.search.sorters.comparators.*;
import net.chesstango.search.sorters.groupsorters.CatchAllNullGroup;
import net.chesstango.search.sorters.groupsorters.CatchAllSortGroup;
import net.chesstango.search.sorters.groupsorters.NoQuietBifurcation;

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

    /**
     * Alpha Beta filters
     *
     */

    default void visit(AlphaBeta alphaBeta) {
    }

    default void visit(AspirationWindows aspirationWindows) {
    }

    default void visit(TranspositionTableRoot transpositionTableRoot) {
    }

    default void visit(AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics) {
    }

    default void visit(AlphaBetaInteriorNodeVisited alphaBetaNodeStatistics) {
    }

    default void visit(AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected) {
    }

    default void visit(AlphaBetaQuiescenceNodeVisited alphaBetaQuiescenceNodeStatistics) {
    }

    default void visit(AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected) {
    }

    default void visit(AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics) {
    }

    default void visit(AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics) {
    }

    default void visit(AlphaBetaLoopNodeStatistics alphaBetaLoopNodeStatistics) {
    }

    default void visit(AlphaBetaEgtbNodeStatistics alphaBetaEgtbNodeStatistics) {
    }

    default void visit(StopProcessingCatch stopProcessingCatch) {
    }

    default void visit(RootMoveEvaluationTracker moveEvaluationTracker) {
    }


    default void visit(RootMoveEvaluationBest rootMoveEvaluationBest) {
    }

    default void visit(RootMoveEvaluationCache rootMoveEvaluationCache) {
    }


    default void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
    }

    default void visit(CalculatePV calculatePV) {
    }

    default void visit(ExtendPV extendPV) {
    }

    default void visit(PropagatePV propagatePV) {
    }

    default void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
    }

    default void visit(TTableArrayPrimitives ttArrayPrimitives) {
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

    default void visit(KillerMoveTracker killerMoveTracker) {
    }

    default void visit(TranspositionTableQ transpositionTableQ) {
    }

    default void visit(PVWalkerFromTT pvWalkerFromTT) {
    }

    default void visit(QuiescenceStandingPat quiescenceStandingPat) {
    }

    default void visit(QuiescenceNull quiescenceNull) {
    }

    default void visit(PrintHtmlDebugHandler printHtmlDebugHandler) {
    }

    default void visit(DebugFilter debugFilter) {
    }

    default void visit(ZobristTracker zobristTracker) {
    }

    default void visit(TTDump ttDump) {
    }

    default void visit(TTLoad ttLoad) {
    }

    default void visit(TranspositionTableLeaf transpositionTableLeaf) {
    }

    /**
     *
     * Setter elements
     */
    default void visit(SetGameToEvaluator setGameToEvaluator) {
    }

    default void visit(PVCalculatorTriangular setTrianglePV) {
    }

    default void visit(NodeCounters nodeCounters) {
    }

    default void visit(DebugNodeTracker debugNodeTracker) {
    }

    default void visit(PrintTxtDebugListener printTxtDebugListener) {
    }

    default void visit(KillerMovesDebug killerMovesDebug) {
    }

    default void visit(TTableNodeDebug tTableNodeDebug) {
    }


    default void visit(TTablePVDebug tTablePVDebug) {
    }

    default void visit(TTableComparatorDebug tTableComparatorDebug) {
    }

    default void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
    }


    default void visit(EvaluatorDebug evaluatorDebug) {
    }

    default void visit(EvaluatorCacheListener evaluatorCacheListener) {
    }

    default void visit(EvaluationCounters evaluationCounters) {
    }

    default void visit(TTListener transpositionTableListener) {
    }

    default void visit(LeafNodeTrap leafNodeTrap) {
    }

    default void visit(SetSearchTimers setSearchTimers) {
    }

    default void visit(LinkEvaluatorVisitor setEvaluatorVisitor) {
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

    default void visit(NodeGroupSorter nodeGroupSorter) {
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

    default void visit(TTableStatisticsNodeCollector tTableStatisticsCollector) {
    }

    default void visit(TTableStatisticsComparatorCollector tTableStatisticsComparatorCollector) {
    }

    default void visit(TTableStatisticsPVCollector tTableStatisticsPVCollector) {
    }

    default void visit(TTableStatisticsFillPercentageCollector tTableStatisticsFillPercentageCollector) {
    }

    default void visit(TTableCounters TTableCounters) {
    }

    default void visit(GameCountersCollector gameCounters) {
    }

    default void visit(DepthCollector maxRegularDepth) {
    }

    /**
     *
     * GroupSorter elements
     */
    default void visit(CatchAllSortGroup catchAllGroup) {
    }

    default void visit(NoQuietBifurcation noQuietGroup) {
    }

    default void visit(PrincipalVariationGroup principalVariationGroup) {
    }

    default void visit(CatchAllNullGroup nullGroup) {
    }
}
