package net.chesstango.search.visitors;

import net.chesstango.board.Game;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchAlgorithm;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.minmax.MinMax;
import net.chesstango.search.smart.negamax.NegaMax;
import net.chesstango.search.smart.negamax.NegaMaxPruning;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

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
    }

    @Override
    public void visit(IterativeDeepening iterativeDeepening) {
        SearchAlgorithm algorithm = iterativeDeepening.getSearchAlgorithm();
        algorithm.accept(this);
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

    @Override
    public void visit(NodeMoveSorter nodeMoveSorter) {
        nodeMoveSorter.setGame(game);

        MoveComparator moveComparator = nodeMoveSorter.getMoveComparator();
        moveComparator.accept(this);
    }

    /**
     *
     * Comparator elements
     */
    @Override
    public void visit(PrincipalVariationComparator principalVariationComparator) {
        principalVariationComparator.setGame(game);
        principalVariationComparator.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionHeadMoveComparator transpositionHeadMoveComparator) {
        transpositionHeadMoveComparator.setGame(game);
        transpositionHeadMoveComparator.getNext().accept(this);
    }

    @Override
    public void visit(TranspositionTailMoveComparator transpositionTailMoveComparator) {
        transpositionTailMoveComparator.setGame(game);
        transpositionTailMoveComparator.getNext().accept(this);
    }

    @Override
    public void visit(QuietComparator quietComparator) {
        quietComparator.getQuietNext().accept(this);
        quietComparator.getNoQuietNext().accept(this);
    }

    @Override
    public void visit(KillerMoveComparator killerMoveComparator) {
        killerMoveComparator.getNext().accept(this);
    }

    @Override
    public void visit(GameEvaluatorCacheComparator gameEvaluatorCacheComparator) {
        gameEvaluatorCacheComparator.setGame(game);
        gameEvaluatorCacheComparator.getNext().accept(this);
    }

    @Override
    public void visit(PromotionComparator promotionComparator) {
        promotionComparator.getNext().accept(this);
    }

    @Override
    public void visit(RecaptureMoveComparator recaptureMoveComparator) {
        recaptureMoveComparator.setGame(game);
        recaptureMoveComparator.getNext().accept(this);
    }

    @Override
    public void visit(MvvLvaComparator mvvLvaComparator) {
        mvvLvaComparator.getNext().accept(this);
    }
}
