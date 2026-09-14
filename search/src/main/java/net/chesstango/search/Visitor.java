package net.chesstango.search;

import net.chesstango.search.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.alphabeta.core.filters.QuiescenceStandingPat;
import net.chesstango.search.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.iterators.PrintHtmlDebugHandler;
import net.chesstango.search.alphabeta.debug.listeners.PrintTxtDebugListener;
import net.chesstango.search.alphabeta.debug.traps.LeafNodeTrap;
import net.chesstango.search.alphabeta.egtb.filters.EgtbEvaluation;
import net.chesstango.search.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.alphabeta.evaluator.EvaluatorCacheAdapter;
import net.chesstango.search.alphabeta.evalcache.EvaluatorCacheDebug;
import net.chesstango.search.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.alphabeta.evaluator.filters.LoopEvaluation;
import net.chesstango.search.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.alphabeta.evaluator.visitors.LinkEvaluatorVisitor;
import net.chesstango.search.alphabeta.killermoves.KillerMovesDebug;
import net.chesstango.search.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.alphabeta.pv.filters.PropagatePV;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.pv.model.PVCalculator;
import net.chesstango.search.alphabeta.pv.model.PVWalkerFromTT;
import net.chesstango.search.alphabeta.quiescence.QuiescenceNull;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationBest;
import net.chesstango.search.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.alphabeta.SearchByDepthImp;
import net.chesstango.search.alphabeta.root.filters.AspirationWindows;
import net.chesstango.search.alphabeta.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.alphabeta.root.filters.StopProcessingCatch;
import net.chesstango.search.alphabeta.statistics.evalcache.EvaluatorCacheCounters;
import net.chesstango.search.alphabeta.statistics.evaluator.EvaluatorCounters;
import net.chesstango.search.alphabeta.statistics.evalcache.EvaluatorCacheStatisticsComparatorCollector;
import net.chesstango.search.alphabeta.statistics.evalcache.EvaluatorCacheStatisticsNodeCollector;
import net.chesstango.search.alphabeta.statistics.evaluator.EvaluatorStatisticsCollector;
import net.chesstango.search.alphabeta.evalcache.listeners.EvaluatorCacheListener;
import net.chesstango.search.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.alphabeta.statistics.game.GameCountersCollector;
import net.chesstango.search.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.alphabeta.statistics.node.filters.*;
import net.chesstango.search.alphabeta.statistics.sorter.SorterCounters;
import net.chesstango.search.alphabeta.statistics.sorter.filters.InteriorNodeSorterPost;
import net.chesstango.search.alphabeta.statistics.sorter.filters.InteriorNodeSorterPre;
import net.chesstango.search.alphabeta.statistics.transposition.*;
import net.chesstango.search.alphabeta.transposition.*;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.filters.*;
import net.chesstango.search.alphabeta.transposition.listeners.TTDump;
import net.chesstango.search.alphabeta.transposition.listeners.TTListener;
import net.chesstango.search.alphabeta.transposition.listeners.TTLoad;
import net.chesstango.search.alphabeta.zobrist.filters.ZobristTracker;
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

    default void visit(IterativeDeepening iterativeDeepening) {
    }

    default void visit(NoIterativeDeepening noIterativeDeepening) {
    }

    /**
     * Facades
     */
    default void visit(SearchByDepthImp searchByDepthImp) {
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

    default void visit(RootNodeStatistics rootNodeStatistics) {
    }

    default void visit(InteriorNodeVisited alphaBetaNodeStatistics) {
    }

    default void visit(InteriorNodeExpected interiorNodeExpected) {
    }

    default void visit(QuiescenceNodeVisited alphaBetaQuiescenceNodeStatistics) {
    }

    default void visit(QuiescenceNodeExpected quiescenceNodeExpected) {
    }

    default void visit(TerminalNodeStatistics terminalNodeStatistics) {
    }

    default void visit(LeafNodeStatistics leafNodeStatistics) {
    }

    default void visit(LoopNodeStatistics loopNodeStatistics) {
    }

    default void visit(EgtbNodeStatistics egtbNodeStatistics) {
    }

    default void visit(StopProcessingCatch stopProcessingCatch) {
    }

    default void visit(RootMoveEvaluationTracker moveEvaluationTracker) {
    }


    default void visit(RootMoveEvaluationBest rootMoveEvaluationBest) {
    }


    default void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
    }

    default void visit(ExtendPV extendPV) {
    }

    default void visit(PropagatePV propagatePV) {
    }

    default void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
    }

    default void visit(TTableArray ttArrayPrimitives) {
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

    default void visit(PVCalculator setTrianglePV) {
    }

    default void visit(NodeCounters nodeCounters) {
    }

    default void visit(SorterCounters sorterCounters) {
    }


    default void visit(InteriorNodeSorterPre interiorNodeSorterPre) {
    }
    default void visit(InteriorNodeSorterPost interiorNodeSorterPost) {
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

    default void visit(TTableComparatorHeadDebug tTableComparatorHeadDebug) {
    }

    default void visit(TTableComparatorTailDebug tTableComparatorTailDebug) {
    }


    default void visit(EvaluatorCacheDebug evaluatorCacheDebug) {
    }


    default void visit(EvaluatorCacheAdapter evaluatorCacheAdapter) {
    }

    default void visit(EvaluatorDebug evaluatorDebug) {
    }

    default void visit(EvaluatorCacheListener evaluatorCacheListener) {
    }

    default void visit(EvaluatorCounters evaluatorCounters) {
    }

    default void visit(EvaluatorCacheCounters evaluatorCacheCounters) {
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

    default void visit(EvaluatorCacheStatisticsNodeCollector evaluatorCacheStatisticsNodeCollector) {
    }

    default void visit(EvaluatorCacheStatisticsComparatorCollector evaluatorCacheStatisticsComparatorCollector) {
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

    default void visit(EvaluatorCacheComparator evaluatorCacheComparator) {
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
