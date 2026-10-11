package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.sorters.comparators.PromotionComparator;
import net.chesstango.search.sorters.comparators.RecaptureMoveComparator;
import net.chesstango.search.sorters.groupsorters.AgregateGroupSorter;
import net.chesstango.search.sorters.groupsorters.CatchAllGroupSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterQuiescenceBuilder extends AbstractMoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private MoveSorterDebug moveSorterDebug;

    private EvaluatorCacheComparator evaluatorCacheComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;

    private PrincipalVariationGroup principalVariationGroup;
    private TranspositionHeadMoveGroup transpositionHeadMoveGroup;
    private TranspositionTailMoveGroup transpositionTailMoveGroup;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterQuiescenceBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter(move -> !move.isQuiet());
    }

    @Override
    public MoveSorterBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return null;
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

    public MoveSorterQuiescenceBuilder withRecaptureSorter() {
        this.withRecaptureSorter = true;
        return this;
    }


    public MoveSorterQuiescenceBuilder withMvvLva() {
        this.withMvvLva = true;
        return this;
    }


    @Override
    protected void buildObjects() {
        defaultMoveComparator = new DefaultMoveComparator();

        if (withIterativeDeepening) {
            principalVariationGroup = new PrincipalVariationGroup();
        }

        if (withTranspositionTable) {
            transpositionHeadMoveGroup = new TranspositionHeadMoveGroup();

            transpositionTailMoveGroup = new TranspositionTailMoveGroup();
        }

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }

        if (withGameEvaluatorCache) {
            evaluatorCacheComparator = new EvaluatorCacheComparator();
        }

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
        }

        promotionComparator = new PromotionComparator();

    }

    @Override
    protected void setupListeners() {
        listenerMediator.add(nodeMoveSorter);

        if (transpositionHeadMoveGroup != null) {
            listenerMediator.add(transpositionHeadMoveGroup);
            nodeMoveSorter.addSortListener(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            listenerMediator.add(transpositionTailMoveGroup);
        }

        if (principalVariationGroup != null) {
            listenerMediator.add(principalVariationGroup);
            nodeMoveSorter.addSortListener(principalVariationGroup);
        }

        if (recaptureMoveComparator != null) {
            listenerMediator.add(recaptureMoveComparator);
            nodeMoveSorter.addSortListener(recaptureMoveComparator);
        }

        if (evaluatorCacheComparator != null) {
            listenerMediator.add(evaluatorCacheComparator);
            nodeMoveSorter.addSortListener(evaluatorCacheComparator);
        }

        if (moveSorterDebug != null) {
            listenerMediator.add(moveSorterDebug);
        }
    }

    @Override
    protected void link() {
        nodeMoveSorter.setGroupSorter(createGroupSorterChain());

        MoveToHashMap moveToZobrist = new MoveToHashMap();

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);

        if (transpositionTailMoveGroup != null) {
            transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
        }

        if (evaluatorCacheComparator != null) {
            evaluatorCacheComparator.setMoveToZobrist(moveToZobrist);
        }
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

    private GroupSorter createGroupSorterChain() {
        AgregateGroupSorter agregateGroupSorter = new AgregateGroupSorter();

        if (principalVariationGroup != null) {
            agregateGroupSorter.addGroupSorter(principalVariationGroup);
        }

        if (transpositionHeadMoveGroup != null) {
            agregateGroupSorter.addGroupSorter(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            agregateGroupSorter.addGroupSorter(transpositionTailMoveGroup);
        }

        agregateGroupSorter.addGroupSorter(new CatchAllGroupSorter());

        return agregateGroupSorter;
    }

    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        chain.add(promotionComparator);

        if (recaptureMoveComparator != null) {
            chain.add(recaptureMoveComparator);
        }

        if (mvvLvaComparator != null) {
            chain.add(mvvLvaComparator);
        }

        if (evaluatorCacheComparator != null) {
            chain.add(evaluatorCacheComparator);
        }

        chain.add(defaultMoveComparator);

        return linkMoveComparatorChain(chain);
    }
}
