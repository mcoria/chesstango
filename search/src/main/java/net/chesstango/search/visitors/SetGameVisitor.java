package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
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
        SearchAlgorithm algorithm = noIterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);

        SearchListenerMediator searchListenerMediator = noIterativeDeepening.getSearchListenerMediator();
        searchListenerMediator.accept(this);
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchAlgorithm algorithm = iterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);

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

    /**
     * Alpha Beta filters
     *
     */

    @Override
    public void visit(MoveEvaluationTracker moveEvaluationTracker) {
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
    public void visit(TTPVReader ttpvReader) {
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
    public void visit(AlphaBetaStatisticsExpected alphaBetaStatisticsExpected) {
        alphaBetaStatisticsExpected.setGame(game);
    }

    @Override
    public void visit(QuiescenceStatisticsExpected quiescenceStatisticsExpected) {
        quiescenceStatisticsExpected.setGame(game);
    }

    @Override
    public void visit(AlphaBetaFlowControl alphaBetaFlowControl) {
        alphaBetaFlowControl.setGame(game);
    }

    @Override
    public void visit(ExtensionFlowControl extensionFlowControl) {
        extensionFlowControl.setGame(game);
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
    public void visit(SetTrianglePV setTrianglePV) {
        setTrianglePV.setGame(game);
    }

    @Override
    public void visit(SetNodeStatistics setNodeStatistics) {
        setNodeStatistics.setGame(game);
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
    public void visit(TranspositionHeadMoveComparatorQ transpositionHeadMoveComparatorQ) {
        transpositionHeadMoveComparatorQ.setGame(game);
    }

    @Override
    public void visit(TranspositionTailMoveComparatorQ transpositionHeadMoveComparatorQ) {
        transpositionHeadMoveComparatorQ.setGame(game);
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setGame(game);
    }

    @Override
    public void visit(RecaptureMoveComparator recaptureMoveComparator) {
        recaptureMoveComparator.setGame(game);
    }
}
