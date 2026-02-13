package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.TTPVReader;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparatorQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;
import net.chesstango.search.smart.minmax.MinMax;
import net.chesstango.search.smart.negamax.NegaMax;
import net.chesstango.search.smart.negamax.NegaMaxPruning;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;

/**
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

    @Override
    public void visit(MinMax minMax) {
        minMax.setGame(game);
    }

    @Override
    public void visit(NegaMax negaMax) {
        negaMax.setGame(game);
    }

    @Override
    public void visit(NegaMaxPruning negaMaxPruning) {
        negaMaxPruning.setGame(game);
        MoveSorter sorter = negaMaxPruning.getMoveSorter();
        sorter.accept(this);
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
    public void visit(SetGameEvaluator setGameEvaluator) {
        setGameEvaluator.setGame(game);
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
