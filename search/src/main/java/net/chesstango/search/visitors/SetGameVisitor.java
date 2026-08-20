package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.pv.model.PVWalkerFromTT;
import net.chesstango.search.smart.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.statistics.game.DepthCollector;
import net.chesstango.search.smart.statistics.game.GameCountersCollector;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaInteriorNodeExpected;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaQuiescenceNodeExpected;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaRootNodeStatistics;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.transposition.filters.*;
import net.chesstango.search.sorters.MoveSorterDebug;
import net.chesstango.search.sorters.NodeGroupSorter;
import net.chesstango.search.sorters.NodeMoveSorter;
import net.chesstango.search.sorters.comparators.RecaptureMoveComparator;

/**
 * Visitor implementation that propagates a Game instance to all components in the search algorithm chain.
 * This visitor traverses the entire search structure and sets the game reference on all components that
 * need access to the current game state, including filters, evaluators, sorters, and comparators.
 * It is typically used during search initialization to ensure all components have the necessary game context.
 *
 * @author Mauricio Coria
 */
public class SetGameVisitor implements Visitor {
    private final Game game;

    public SetGameVisitor(Game game) {
        this.game = game;
    }


    @Override
    public void visit(NoIterativeDeepening noIterativeDeepening) {
        SearchListenerMediator searchListenerMediator = noIterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchListenerMediator searchListenerMediator = iterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    /**
     * Alpha Beta filters
     *
     */
    @Override
    public void visit(AlphaBetaEvaluation alphaBetaEvaluation) {
        alphaBetaEvaluation.setGame(game);
    }

    @Override
    public void visit(QuiescenceStandingPat quiescenceStandingPat) {
        quiescenceStandingPat.setGame(game);
    }

    @Override
    public void visit(DebugFilter debugFilter) {
        debugFilter.setGame(game);
    }

    @Override
    public void visit(RootMoveEvaluationTracker moveEvaluationTracker) {
        moveEvaluationTracker.setGame(game);
    }

    @Override
    public void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        rootMoveEvaluationCollection.setGame(game);
    }

    @Override
    public void visit(KillerMoveTracker killerMoveTracker) {
        killerMoveTracker.setGame(game);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setGame(game);
    }

    @Override
    public void visit(ExtendPV extendPV) {
        extendPV.setGame(game);
    }

    @Override
    public void visit(TranspositionTable transpositionTable) {
        transpositionTable.setGame(game);
    }

    @Override
    public void visit(TranspositionTableQ transpositionTableQ) {
        transpositionTableQ.setGame(game);
    }

    @Override
    public void visit(TranspositionTableTerminal transpositionTableTerminal) {
        transpositionTableTerminal.setGame(game);
    }

    @Override
    public void visit(TranspositionTableLeaf transpositionTableLeaf) {
        transpositionTableLeaf.setGame(game);
    }

    @Override
    public void visit(PVWalkerFromTT pvWalkerFromTT) {
        pvWalkerFromTT.setGame(game);
    }

    @Override
    public void visit(AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics) {
        alphaBetaRootNodeStatistics.setGame(game);
    }

    @Override
    public void visit(AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected) {
        alphaBetaInteriorNodeExpected.setGame(game);
    }

    @Override
    public void visit(AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected) {
        alphaBetaQuiescenceNodeExpected.setGame(game);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        alphaBetaFlowControl.setGame(game);
    }

    @Override
    public void visit(StopProcessingCatch stopProcessingCatch) {
        stopProcessingCatch.setGame(game);
    }

    /**
     *
     * Setter elements
     */
    @Override
    public void visit(SetGameToEvaluator setGameToEvaluator) {
        setGameToEvaluator.setGame(game);
    }

    @Override
    public void visit(SetGameToEndGameTableBase setGameToEndGameTableBase) {
        setGameToEndGameTableBase.setGame(game);
    }

    @Override
    public void visit(PVCalculatorTriangular setTrianglePV) {
        setTrianglePV.setGame(game);
    }

    @Override
    public void visit(GameCountersCollector gameCounters) {
        gameCounters.setGame(game);
    }

    @Override
    public void visit(DepthCollector maxRegularDepth) {
        maxRegularDepth.setGame(game);
    }

    /**
     *
     * Sorter elements
     */

    @Override
    public void visit(NodeMoveSorter nodeMoveSorter) {
        nodeMoveSorter.setGame(game);
    }

    @Override
    public void visit(MoveSorterDebug moveSorterDebug) {
        moveSorterDebug.setGame(game);
    }

    @Override
    public void visit(NodeGroupSorter nodeGroupSorter) {
        nodeGroupSorter.setGame(game);
    }

    /**
     *
     * Comparator elements
     */
    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setGame(game);
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setGame(game);
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setGame(game);
    }

    @Override
    public void visit(RecaptureMoveComparator recaptureMoveComparator) {
        recaptureMoveComparator.setGame(game);
    }

    @Override
    public void visit(PrincipalVariationGroup principalVariationGroup) {
        principalVariationGroup.setGame(game);
    }

}
