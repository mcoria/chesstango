package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.comparators.*;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllSortGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder extends AbstractMoveSorterBuilder {
    private final QuietComparator quietComparator;
    private final DefaultMoveComparator defaultMoveComparator;
    private final NodeGroupSorter nodeGroupSorter;
    private final CatchAllSortGroup catchAllSortGroup;

    private SearchListenerMediator searchListenerMediator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private KillerMoveComparator killerMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;

    private PrincipalVariationGroup principalVariationGroup;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterBuilder() {
        this.quietComparator = new QuietComparator();
        this.recaptureMoveComparator = new RecaptureMoveComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
        this.nodeGroupSorter = new NodeGroupSorter();
        this.catchAllSortGroup = new CatchAllSortGroup();
    }

    public MoveSorterBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }

    public MoveSorterBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public MoveSorterBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterBuilder withKillerMoveSorter() {
        this.withKillerMoveSorter = true;
        return this;
    }

    public MoveSorterBuilder withRecaptureSorter() {
        this.withRecaptureSorter = true;
        return this;
    }

    public MoveSorterBuilder withMvvLva() {
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

            principalVariationComparator = new PrincipalVariationComparator();
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

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
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

        if (principalVariationComparator != null) {
            searchListenerMediator.add(principalVariationComparator);
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

        if (principalVariationGroup != null) {
            searchListenerMediator.add(principalVariationGroup);
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
