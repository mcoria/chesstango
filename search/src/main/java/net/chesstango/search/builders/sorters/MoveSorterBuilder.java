package net.chesstango.search.builders.sorters;

import net.chesstango.evaluation.EvaluatorCacheRead;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorCacheDebug;
import net.chesstango.search.smart.alphabeta.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.alphabeta.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.alphabeta.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.alphabeta.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder extends AbstractMoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private final QuietComparator quietComparator;
    private final DefaultMoveComparator defaultMoveComparator;
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

    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.quietComparator = new QuietComparator();
        this.recaptureMoveComparator = new RecaptureMoveComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
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

    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();

        nodeMoveSorter.setMoveComparator(createComparatorChain());

        return createChain();
    }

    private MoveSorter createChain() {
        List<MoveSorter> chain = new LinkedList<>();

        if (moveSorterDebug != null) {
            chain.add(moveSorterDebug);
        }

        chain.add(nodeMoveSorter);

        return buildChain(chain);
    }

    private void buildObjects() {
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

        promotionComparator = new PromotionComparator();
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(nodeMoveSorter);
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
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(principalVariationComparator);
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        MoveComparator chainTail = buildChainTail();
        quietComparator.setNoQuietNext(buildNoQuietNext(chainTail));
        quietComparator.setQuietNext(buildQuietNext(chainTail));

        chain.add(quietComparator);

        return linkComparatorChain(chain);
    }


    private MoveComparator buildNoQuietNext(MoveComparator chainTail) {
        List<MoveComparator> chain = new LinkedList<>();

        chain.add(promotionComparator);

        if (recaptureMoveComparator != null) {
            chain.add(recaptureMoveComparator);
        }

        if (mvvLvaComparator != null) {
            chain.add(mvvLvaComparator);
        }

        chain.add(chainTail);

        return linkComparatorChain(chain);
    }

    private MoveComparator buildQuietNext(MoveComparator chainTail) {
        List<MoveComparator> chain = new LinkedList<>();

        if (killerMoveComparator != null) {
            chain.add(killerMoveComparator);
        }

        chain.add(chainTail);

        return linkComparatorChain(chain);
    }

    private MoveComparator buildChainTail() {
        List<MoveComparator> chain = new LinkedList<>();

        if (gameEvaluatorCacheComparator != null) {
            chain.add(gameEvaluatorCacheComparator);
        }

        chain.add(defaultMoveComparator);

        return linkComparatorChain(chain);
    }
}
