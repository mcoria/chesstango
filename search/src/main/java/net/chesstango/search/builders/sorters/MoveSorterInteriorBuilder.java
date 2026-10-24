package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.killermoves.sorters.KillerMovePredicate;
import net.chesstango.search.alphabeta.killermoves.sorters.comparators.KillerMoveComparator;
import net.chesstango.search.alphabeta.pv.sorters.PrincipalVariationPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionHeadPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionTailPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.sorters.comparators.PromotionComparator;
import net.chesstango.search.sorters.comparators.RecaptureMoveComparator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterInteriorBuilder extends AbstractMoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private MoveSorterDebug moveSorterDebug;

    private EvaluatorCacheComparator evaluatorCacheComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;

    private PrincipalVariationPredicate principalVariationPredicate;
    private TranspositionHeadPredicate transpositionHeadPredicate;

    private TranspositionTailPredicate transpositionTailPredicate;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;

    private KillerMovePredicate killerMovePredicate;
    private KillerMoveComparator killerMoveComparator;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMove;
    private boolean withRecapture;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterInteriorBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
    }

    @Override
    public MoveSorterInteriorBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterInteriorBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterInteriorBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterInteriorBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterInteriorBuilder withKillerMove() {
        this.withKillerMove = true;
        return this;
    }

    public MoveSorterInteriorBuilder withRecapture() {
        this.withRecapture = true;
        return this;
    }

    public MoveSorterInteriorBuilder withMvvLva() {
        this.withMvvLva = true;
        return this;
    }


    @Override
    protected void buildObjects() {
        promotionComparator = new PromotionComparator();

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }

        if (withIterativeDeepening) {
            principalVariationPredicate = new PrincipalVariationPredicate();
        }

        if (withTranspositionTable) {
            transpositionHeadPredicate = new TranspositionHeadPredicate();

            transpositionTailPredicate = new TranspositionTailPredicate();
            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
        }

        if (withGameEvaluatorCache) {
            evaluatorCacheComparator = new EvaluatorCacheComparator();
        }

        if (withKillerMove) {
            killerMovePredicate = new KillerMovePredicate();
            killerMoveComparator = new KillerMoveComparator();
        }

        if (withRecapture) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
        }
    }

    @Override
    protected void setupListeners() {
        listenerMediator.add(nodeMoveSorter);

        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }

        if (promotionComparator != null) {
            listenerMediator.add(promotionComparator);
        }

        if (transpositionHeadPredicate != null) {
            listenerMediator.add(transpositionHeadPredicate);
        }

        if (transpositionTailPredicate != null) {
            listenerMediator.add(transpositionTailPredicate);
        }

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationPredicate != null) {
            listenerMediator.add(principalVariationPredicate);
        }

        if (recaptureMoveComparator != null) {
            listenerMediator.add(recaptureMoveComparator);
        }

        if (evaluatorCacheComparator != null) {
            listenerMediator.add(evaluatorCacheComparator);
        }

        if (killerMovePredicate != null) {
            listenerMediator.add(killerMovePredicate);
        }

        if (killerMoveComparator != null) {
            listenerMediator.add(killerMoveComparator);
        }
    }

    @Override
    protected void link() {
        MoveToHashMap moveToZobrist = new MoveToHashMap();

        if (transpositionTailPredicate != null) {
            transpositionTailPredicate.setMoveToZobrist(moveToZobrist);
        }

        if (transpositionTailMoveComparator != null) {
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);
        }

        if (evaluatorCacheComparator != null) {
            evaluatorCacheComparator.setMoveToZobrist(moveToZobrist);
        }

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
        nodeMoveSorter.setSorterListeners(createSortListeners());
        nodeMoveSorter.setMoveSorterStages(createGroupSorterChain());
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeMoveSorter);

        return linkMoveSorterChain(chain);
    }

    private List<MoveSorterListener> createSortListeners() {
        List<MoveSorterListener> moveSorterListeners = new ArrayList<>();

        if (transpositionHeadPredicate != null) {
            moveSorterListeners.add(transpositionHeadPredicate);
        }

        if (principalVariationPredicate != null) {
            moveSorterListeners.add(principalVariationPredicate);
        }

        if (recaptureMoveComparator != null) {
            moveSorterListeners.add(recaptureMoveComparator);
        }

        if (evaluatorCacheComparator != null) {
            moveSorterListeners.add(evaluatorCacheComparator);
        }

        if (killerMovePredicate != null) {
            moveSorterListeners.add(killerMovePredicate);
        }

        if (killerMoveComparator != null) {
            moveSorterListeners.add(killerMoveComparator);
        }

        return moveSorterListeners;
    }


    private List<NodeMoveSorterStage> createGroupSorterChain() {
        List<NodeMoveSorterStage> nodeMoveSorterStages = new ArrayList<>();

        if (principalVariationPredicate != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createPrincipalVariationStage(principalVariationPredicate));
        }

        if (transpositionHeadPredicate != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionHeadStage(transpositionHeadPredicate));
        }

        if (transpositionTailPredicate != null && transpositionTailMoveComparator != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionTailStage(transpositionTailPredicate, transpositionTailMoveComparator));
        }

        if (killerMovePredicate != null && killerMoveComparator != null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createKillerMoveStage(killerMovePredicate, killerMoveComparator));
        }

        nodeMoveSorterStages.add(NodeMoveSorterStage.createCatchAllStage());

        return nodeMoveSorterStages;
    }
}
