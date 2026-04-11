package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.comparators.QuietComparator;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterGroupInteriorBuilder extends AbstractMoveSorterBuilder {
    private final QuietComparator quietComparator;
    private final DefaultMoveComparator defaultMoveComparator;
    private final NodeGroupSorter nodeGroupSorter;
    private final CatchAllSortGroup catchAllSortGroup;

    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private KillerMoveComparator killerMoveComparator;

    private PrincipalVariationGroup principalVariationGroup;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterGroupInteriorBuilder() {
        this.quietComparator = new QuietComparator();
        this.recaptureMoveComparator = new RecaptureMoveComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
        this.nodeGroupSorter = new NodeGroupSorter();
        this.catchAllSortGroup = new CatchAllSortGroup();
    }

    @Override
    public MoveSorterGroupInteriorBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterGroupInteriorBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterGroupInteriorBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterGroupInteriorBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterGroupInteriorBuilder withKillerMoveSorter() {
        this.withKillerMoveSorter = true;
        return this;
    }

    public MoveSorterGroupInteriorBuilder withRecaptureSorter() {
        this.withRecaptureSorter = true;
        return this;
    }

    public MoveSorterGroupInteriorBuilder withMvvLva() {
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

        if (withKillerMoveSorter) {
            killerMoveComparator = new KillerMoveComparator();
        }

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }
    }

    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(nodeGroupSorter);

        searchListenerMediator.add(quietComparator);

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

        if (killerMoveComparator != null) {
            searchListenerMediator.add(killerMoveComparator);
        }
    }

    @Override
    protected void linkObjects() {
        catchAllSortGroup.setMoveComparator(defaultMoveComparator);

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
            principalVariationGroup.setNext(catchAllSortGroup);
            head = principalVariationGroup;
        }
        if (head == null) {
            head = catchAllSortGroup;
        }

        return head;
    }
}
