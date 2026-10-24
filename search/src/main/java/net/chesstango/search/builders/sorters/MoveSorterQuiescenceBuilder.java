package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.pv.sorters.PrincipalVariationPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionHeadPredicate;
import net.chesstango.search.alphabeta.transposition.sorters.TranspositionTailPredicate;
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
public class MoveSorterQuiescenceBuilder extends AbstractMoveSorterBuilder {
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

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecapture;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterQuiescenceBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter(move -> !move.isQuiet());
    }

    @Override
    public MoveSorterBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterQuiescenceBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterQuiescenceBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterQuiescenceBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterQuiescenceBuilder withRecapture() {
        this.withRecapture = true;
        return this;
    }


    public MoveSorterQuiescenceBuilder withMvvLva() {
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

        if (transpositionTailPredicate != null && transpositionTailMoveComparator!=null) {
            nodeMoveSorterStages.add(NodeMoveSorterStage.createTranspositionTailStage(transpositionTailPredicate, transpositionTailMoveComparator));
        }

        nodeMoveSorterStages.add(NodeMoveSorterStage.createCatchAllStage());

        return nodeMoveSorterStages;
    }}
