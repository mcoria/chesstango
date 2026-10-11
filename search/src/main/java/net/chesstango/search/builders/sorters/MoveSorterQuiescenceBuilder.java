package net.chesstango.search.builders.sorters;

import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.alphabeta.evalcache.comparators.EvaluatorCacheComparator;
import net.chesstango.search.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionHeadMoveGroup;
import net.chesstango.search.alphabeta.transposition.groupsorters.TranspositionTailMoveGroup;
import net.chesstango.search.sorters.*;
import net.chesstango.search.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.sorters.comparators.PromotionComparator;
import net.chesstango.search.sorters.comparators.RecaptureMoveComparator;
import net.chesstango.search.sorters.groupsorters.AgregateGroup;
import net.chesstango.search.sorters.groupsorters.CatchAllGroup;

import java.util.Comparator;
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

    private AgregateGroup agregateGroup;
    private CatchAllGroup catchAllGroup;
    private PrincipalVariationGroup principalVariationGroup;
    private TranspositionHeadMoveGroup transpositionHeadMoveGroup;
    private TranspositionTailMoveGroup transpositionTailMoveGroup;

    private TranspositionTailMoveComparator transpositionTailMoveComparator;

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
        agregateGroup = new AgregateGroup();
        catchAllGroup = new CatchAllGroup();

        defaultMoveComparator = new DefaultMoveComparator();

        if (withIterativeDeepening) {
            principalVariationGroup = new PrincipalVariationGroup();
        }

        if (withTranspositionTable) {
            transpositionHeadMoveGroup = new TranspositionHeadMoveGroup();

            transpositionTailMoveGroup = new TranspositionTailMoveGroup();
            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
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

        if (transpositionTailMoveComparator != null) {
            listenerMediator.add(transpositionTailMoveComparator);
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
        MoveToHashMap moveToZobrist = new MoveToHashMap();

        nodeMoveSorter.setMoveToZobrist(moveToZobrist);
        nodeMoveSorter.setGroupSorter(createGroupSorterChain());

        if (transpositionTailMoveGroup != null && transpositionTailMoveComparator != null) {
            transpositionTailMoveGroup.setMoveToZobrist(moveToZobrist);
            transpositionTailMoveComparator.setMoveToZobrist(moveToZobrist);

            Comparator<Move> moveComparator = transpositionTailMoveComparator
                    .thenComparing(new DefaultMoveComparator());

            transpositionTailMoveGroup.setMoveComparator(moveComparator.reversed());
        }

        if (evaluatorCacheComparator != null) {
            evaluatorCacheComparator.setMoveToZobrist(moveToZobrist);
        }

        catchAllGroup.setMoveComparator(new DefaultMoveComparator().reversed());
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
        if (principalVariationGroup != null) {
            agregateGroup.addGroup(principalVariationGroup);
        }

        if (transpositionHeadMoveGroup != null) {
            agregateGroup.addGroup(transpositionHeadMoveGroup);
        }

        if (transpositionTailMoveGroup != null) {
            agregateGroup.addGroup(transpositionTailMoveGroup);
        }

        agregateGroup.addGroup(catchAllGroup);

        return agregateGroup;
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
