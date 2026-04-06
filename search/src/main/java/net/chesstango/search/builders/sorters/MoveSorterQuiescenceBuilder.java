package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.*;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.smart.sorters.comparators.PromotionComparator;
import net.chesstango.search.smart.sorters.comparators.RecaptureMoveComparator;
import net.chesstango.search.smart.sorters.groupsorters.CatchAllGroup;
import net.chesstango.search.smart.sorters.groupsorters.NoQuietGroup;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterQuiescenceBuilder extends AbstractMoveSorterBuilder {
    private final NodeGroupSorter nodeGroupSorter;
    private SearchListenerMediator searchListenerMediator;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;

    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;


    private NoQuietGroup noQuietGroup;;
    private PrincipalVariationGroup principalVariationGroup;
    private CatchAllGroup catchAllGroup;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterQuiescenceBuilder() {
        this.nodeGroupSorter = new NodeGroupSorter();
        this.noQuietGroup = new NoQuietGroup();
        this.catchAllGroup = new CatchAllGroup();
    }

    public MoveSorterQuiescenceBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }

    public MoveSorterQuiescenceBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
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

    public MoveSorterQuiescenceBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
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


    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();

        nodeGroupSorter.setGroupSorter(createGroupSorterChain());

        return createChain();
    }

    private MoveSorter createChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeGroupSorter);

        return buildChain(chain);
    }

    private void buildObjects() {
        defaultMoveComparator = new DefaultMoveComparator();

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

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
        }

        promotionComparator = new PromotionComparator();

    }

    private void setupListenerMediator() {
        searchListenerMediator.add(nodeGroupSorter);

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

        if (principalVariationGroup != null) {
            searchListenerMediator.add(principalVariationGroup);
        }
    }


    private GroupSorter createGroupSorterChain() {
        List<GroupSorter> chain = new LinkedList<>();

        chain.add(noQuietGroup);
        if (principalVariationGroup != null) {
            chain.add(principalVariationGroup);
        }
        chain.add(catchAllGroup);

        return linkGroupSorterChain(chain);
    }
}
