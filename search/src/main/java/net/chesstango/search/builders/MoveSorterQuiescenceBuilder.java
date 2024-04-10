package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.features.evaluator.GameEvaluatorCacheDebug;
import net.chesstango.search.smart.features.evaluator.comparators.GameEvaluatorCacheComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionHeadMoveComparator;
import net.chesstango.search.smart.features.transposition.comparators.TranspositionTailMoveComparator;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterQuiescenceBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private SmartListenerMediator smartListenerMediator;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;
    private MoveSorterDebug moveSorterDebug;
    private GameEvaluatorCacheDebug gameEvaluatorCacheDebug;
    private GameEvaluatorCache gameEvaluatorCache;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;

    public MoveSorterQuiescenceBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter(move -> !move.isQuiet());
    }

    public MoveSorterQuiescenceBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public MoveSorterQuiescenceBuilder withGameEvaluatorCache(GameEvaluatorCache gameEvaluatorCache) {
        this.gameEvaluatorCache = gameEvaluatorCache;
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


        MoveSorter moveSorter = nodeMoveSorter;
        nodeMoveSorter.setMoveComparator(createComparatorChain());

        if (moveSorterDebug != null) {
            moveSorterDebug.setMoveSorterImp(moveSorter);
            moveSorter = moveSorterDebug;
        }

        return moveSorter;
    }

    private void buildObjects() {
        defaultMoveComparator = new DefaultMoveComparator();

        if (withTranspositionTable) {
            transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
            transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);

            principalVariationComparator = new PrincipalVariationComparator();
        }

        if (withDebugSearchTree) {
            moveSorterDebug = new MoveSorterDebug();
            gameEvaluatorCacheDebug = new GameEvaluatorCacheDebug();
            gameEvaluatorCacheDebug.setGameEvaluatorCacheRead(gameEvaluatorCache);
        }

        if (gameEvaluatorCache != null) {
            gameEvaluatorCacheComparator = new GameEvaluatorCacheComparator();
            if (withDebugSearchTree) {
                gameEvaluatorCacheComparator.setGameEvaluatorCacheRead(gameEvaluatorCacheDebug);
            } else {
                gameEvaluatorCacheComparator.setGameEvaluatorCacheRead(gameEvaluatorCache);
            }
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
        smartListenerMediator.add(nodeMoveSorter);

        if (transpositionHeadMoveComparator != null) {
            smartListenerMediator.add(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            smartListenerMediator.add(transpositionTailMoveComparator);
        }

        if (principalVariationComparator != null) {
            smartListenerMediator.add(principalVariationComparator);
        }

        if (recaptureMoveComparator != null) {
            smartListenerMediator.add(recaptureMoveComparator);
        }

        if (gameEvaluatorCacheComparator != null) {
            smartListenerMediator.add(gameEvaluatorCacheComparator);
        }

        if (moveSorterDebug != null) {
            smartListenerMediator.add(moveSorterDebug);
        }

        if (gameEvaluatorCacheDebug != null) {
            smartListenerMediator.add(gameEvaluatorCacheDebug);
        }

    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(principalVariationComparator);
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        chain.add(promotionComparator);

        if (recaptureMoveComparator != null) {
            chain.add(recaptureMoveComparator);
        }

        if (mvvLvaComparator != null) {
            chain.add(mvvLvaComparator);
        }

        if (gameEvaluatorCacheComparator != null) {
            chain.add(gameEvaluatorCacheComparator);
        }

        chain.add(defaultMoveComparator);

        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);

            if (currentComparator instanceof TranspositionHeadMoveComparator) {
                transpositionHeadMoveComparator.setNext(next);
            } else if (currentComparator instanceof TranspositionTailMoveComparator) {
                transpositionTailMoveComparator.setNext(next);
            } else if (currentComparator instanceof RecaptureMoveComparator) {
                recaptureMoveComparator.setNext(next);
            } else if (currentComparator instanceof GameEvaluatorCacheComparator) {
                gameEvaluatorCacheComparator.setNext(next);
            } else if (currentComparator instanceof MvvLvaComparator) {
                mvvLvaComparator.setNext(next);
            } else if (currentComparator instanceof PromotionComparator) {
                promotionComparator.setNext(next);
            } else if (currentComparator instanceof PrincipalVariationComparator) {
                principalVariationComparator.setNext(next);
            } else {
                throw new RuntimeException("Unknow MoveComparator");
            }
        }


        return chain.get(0);
    }
}
