package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllNullGroup;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;
import net.chesstango.search.smart.sorters.groupsorters.NoQuietBifurcation;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterGroupQuiescenceBuilder extends AbstractMoveSorterBuilder {
    private final NodeGroupSorter nodeGroupSorter;
    private final NoQuietBifurcation noQuietBifurcation;
    private final CatchAllSortGroup catchAllGroup;
    private final CatchAllNullGroup nullGroup;
    private final DefaultMoveComparator defaultMoveComparator;

    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;

    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;

    private PrincipalVariationGroup principalVariationGroup;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterGroupQuiescenceBuilder() {
        this.nodeGroupSorter = new NodeGroupSorter();
        this.noQuietBifurcation = new NoQuietBifurcation(true);
        this.catchAllGroup = new CatchAllSortGroup();
        this.nullGroup = new CatchAllNullGroup();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public MoveSorterGroupQuiescenceBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterGroupQuiescenceBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterGroupQuiescenceBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterGroupQuiescenceBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterGroupQuiescenceBuilder withRecaptureSorter() {
        this.withRecaptureSorter = true;
        return this;
    }


    public MoveSorterGroupQuiescenceBuilder withMvvLva() {
        this.withMvvLva = true;
        return this;
    }


    @Override
    protected void buildObjects() {
        if (withIterativeDeepening) {
            principalVariationGroup = new PrincipalVariationGroup();
        }

        if (withTranspositionTable) {
            transpositionHeadMoveComparator = new TranspositionHeadMoveComparator();
            transpositionTailMoveComparator = new TranspositionTailMoveComparator();
        }

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
        }

        if (withGameEvaluatorCache) {
            gameEvaluatorCacheComparator = new GameEvaluatorCacheComparator();
        }

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }
    }

    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(nodeGroupSorter);

        if (transpositionHeadMoveComparator != null) {
            searchListenerMediator.add(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            searchListenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationGroup != null) {
            searchListenerMediator.add(principalVariationGroup);
        }

        if (recaptureMoveComparator != null) {
            searchListenerMediator.add(recaptureMoveComparator);
        }

        if (gameEvaluatorCacheComparator != null) {
            searchListenerMediator.add(gameEvaluatorCacheComparator);
        }

        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }
    }

    @Override
    protected void linkObjects() {
        catchAllGroup.setMoveComparator(defaultMoveComparator);

        noQuietBifurcation.setNoQuietGroup(catchAllGroup);
        noQuietBifurcation.setQuietGroup(nullGroup);

        nodeGroupSorter.setGroupSorter(createGroupSorterChain());
    }

    @Override
    protected MoveSorter buildSorterChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeGroupSorter);

        return linkMoveSorterChain(chain);
    }


    private GroupSorter createGroupSorterChain() {
        GroupSorter head = null;

        if (principalVariationGroup != null) {
            principalVariationGroup.setNext(noQuietBifurcation);
            head = principalVariationGroup;
        }
        if (head == null) {
            head = noQuietBifurcation;
        }

        return head;
    }
}
