package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.pv.groupsorters.PrincipalVariationGroup;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeGroupSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import net.chesstango.search.smart.sorters.comparators.MvvLvaComparator;
import net.chesstango.search.smart.sorters.comparators.PromotionComparator;
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

    private SearchListenerMediator searchListenerMediator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;

    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;

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

    public MoveSorterGroupQuiescenceBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }

    public MoveSorterGroupQuiescenceBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
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

    public MoveSorterGroupQuiescenceBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
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
        promotionComparator = new PromotionComparator();


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
