package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
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

    private PrincipalVariationGroup principalVariationGroup;
    private TranspositionHeadMoveGroup transpositionHeadMoveGroup;

    private TranspositionTailMoveGroup transpositionTailMoveGroup;
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
            principalVariationGroup = new PrincipalVariationGroup();
        }

        if (withTranspositionTable) {
            transpositionHeadMoveGroup = new TranspositionHeadMoveGroup();

            transpositionTailMoveGroup = new TranspositionTailMoveGroup();
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

        if (transpositionHeadMoveGroup != null) {
            listenerMediator.add(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            listenerMediator.add(transpositionTailMoveGroup);
        }

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationGroup != null) {
            listenerMediator.add(principalVariationGroup);
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

        if (transpositionTailMoveGroup != null) {
            transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
        }

        if (transpositionTailMoveComparator != null) {
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);
        }

        if (evaluatorCacheComparator != null) {
            evaluatorCacheComparator.setMoveToZobrist(moveToZobrist);
        }

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
        nodeMoveSorter.setSortListeners(createSortListeners());
        nodeMoveSorter.setGroupOrders(createGroupSorterChain());
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

    private List<SortListener> createSortListeners() {
        List<SortListener> sortListeners = new ArrayList<>();

        if (transpositionHeadMoveGroup != null) {
            sortListeners.add(transpositionHeadMoveGroup);
        }

        if (principalVariationGroup != null) {
            sortListeners.add(principalVariationGroup);
        }

        if (recaptureMoveComparator != null) {
            sortListeners.add(recaptureMoveComparator);
        }

        if (evaluatorCacheComparator != null) {
            sortListeners.add(evaluatorCacheComparator);
        }

        return sortListeners;
    }


    private List<GroupOrder> createGroupSorterChain() {
        List<GroupOrder> groupOrders = new ArrayList<>();

        if (principalVariationGroup != null) {
            groupOrders.add(GroupOrder.createPrincipalVariation(principalVariationGroup));
        }

        if (transpositionHeadMoveGroup != null) {
            groupOrders.add(GroupOrder.createTranspositionHeadMoveGroup(transpositionHeadMoveGroup));
        }

        if (transpositionTailMoveGroup != null && transpositionTailMoveComparator!=null) {
            groupOrders.add(GroupOrder.createTranspositionTailMoveGroup(transpositionTailMoveGroup, transpositionTailMoveComparator));
        }

        groupOrders.add(GroupOrder.createCatchAll());

        return groupOrders;
    }}
