package net.chesstango.search.builders.sorters;

import net.chesstango.search.smart.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.MoveSorter;
import net.chesstango.search.sorters.MoveSorterDebug;
import net.chesstango.search.sorters.NodeMoveSorter;
import net.chesstango.search.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterInteriorBuilder extends AbstractMoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private final QuietComparator quietComparator;
    private final DefaultMoveComparator defaultMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MoveSorterDebug moveSorterDebug;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private KillerMoveComparator killerMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;

    private boolean withIterativeDeepening;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMove;
    private boolean withRecapture;
    private boolean withMvvLva;
    private boolean withGameEvaluatorCache;

    public MoveSorterInteriorBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.quietComparator = new QuietComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public MoveSorterInteriorBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MoveSorterInteriorBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public MoveSorterInteriorBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public MoveSorterInteriorBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public MoveSorterInteriorBuilder withKillerMove() {
        this.withKillerMove = true;
        return this;
    }

    public MoveSorterInteriorBuilder withRecapture() {
        this.withRecapture = true;
        return this;
    }

    public MoveSorterInteriorBuilder withMvvLva() {
        this.withMvvLva = true;
        return this;
    }


    @Override
    protected void buildObjects() {
        if (withIterativeDeepening) {
            principalVariationComparator = new PrincipalVariationComparator();
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

        if (withKillerMove) {
            killerMoveComparator = new KillerMoveComparator();
        }

        if (withRecapture) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withMvvLva) {
            mvvLvaComparator = new MvvLvaComparator();
        }

        promotionComparator = new PromotionComparator();
    }

    @Override
    protected void setupListeners() {
        searchListenerMediator.add(nodeMoveSorter);
        searchListenerMediator.add(quietComparator);

        if (transpositionHeadMoveComparator != null) {
            searchListenerMediator.add(transpositionHeadMoveComparator);
            nodeMoveSorter.addSortListener(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            searchListenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationComparator != null) {
            searchListenerMediator.add(principalVariationComparator);
            nodeMoveSorter.addSortListener(principalVariationComparator);
        }

        if (recaptureMoveComparator != null) {
            searchListenerMediator.add(recaptureMoveComparator);
            nodeMoveSorter.addSortListener(recaptureMoveComparator);
        }

        if (gameEvaluatorCacheComparator != null) {
            searchListenerMediator.add(gameEvaluatorCacheComparator);
            nodeMoveSorter.addSortListener(gameEvaluatorCacheComparator);
        }

        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        if (killerMoveComparator != null) {
            searchListenerMediator.add(killerMoveComparator);
            nodeMoveSorter.addSortListener(killerMoveComparator);
        }
    }

    @Override
    protected void link() {
        nodeMoveSorter.setMoveComparator(createComparatorChain());
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


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (principalVariationComparator != null) {
            chain.add(principalVariationComparator);
        }

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        MoveComparator chainTail = buildChainTail();
        quietComparator.setNoQuietNext(buildNoQuietNext(chainTail));
        quietComparator.setQuietNext(buildQuietNext(chainTail));

        chain.add(quietComparator);

        return linkMoveComparatorChain(chain);
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

        return linkMoveComparatorChain(chain);
    }

    private MoveComparator buildQuietNext(MoveComparator chainTail) {
        List<MoveComparator> chain = new LinkedList<>();

        if (killerMoveComparator != null) {
            chain.add(killerMoveComparator);
        }

        chain.add(chainTail);

        return linkMoveComparatorChain(chain);
    }

    private MoveComparator buildChainTail() {
        List<MoveComparator> chain = new LinkedList<>();

        if (gameEvaluatorCacheComparator != null) {
            chain.add(gameEvaluatorCacheComparator);
        }

        chain.add(defaultMoveComparator);

        return linkMoveComparatorChain(chain);
    }
}
