package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.root.filters.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.smart.alphabeta.statistics.game.GameCountersCollector;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaQuiescenceNodeExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaRootNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;

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
     * Facades
     */
    @Override
    public void visit(AlphaBetaFacade alphaBetaFacade) {
        alphaBetaFacade.setGame(game);
    }

    @Override
    public void visit(RootMoveEvaluationCollection rootMoveEvaluationCollection) {
        rootMoveEvaluationCollection.setGame(game);
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
    public void visit(Quiescence quiescence) {
        quiescence.setGame(game);
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
    public void visit(KillerMoveTracker killerMoveTracker) {
        killerMoveTracker.setGame(game);
    }

    @Override
    public void visit(TranspositionTableRoot transpositionTableRoot) {
        transpositionTableRoot.setGame(game);
    }

    @Override
    public void visit(TriangularPV triangularPV) {
        triangularPV.setGame(game);
    }

    @Override
    public void visit(PVCalculatorTransposition ttpvReader) {
        ttpvReader.setGame(game);
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

    @Override
    public void visit(SetSearchTracker setSearchTracker) {
        setSearchTracker.setGame(game);
    }

    /**
     *
     * Sorter elements
     */

    @Override
    public void visit(RootMoveSorter rootMoveSorter) {
        rootMoveSorter.setGame(game);
    }

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
    public void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
        transpositionTailMoveComparator.setGame(game);
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
