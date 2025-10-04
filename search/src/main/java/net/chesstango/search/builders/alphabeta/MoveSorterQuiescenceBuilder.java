package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.pv.comparators.PrincipalVariationComparator;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.features.evaluator.EvaluatorCacheDebug;
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
    private SearchListenerMediator searchListenerMediator;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private MvvLvaComparator mvvLvaComparator;
    private PromotionComparator promotionComparator;
    private PrincipalVariationComparator principalVariationComparator;
    private MoveSorterDebug moveSorterDebug;
    private EvaluatorCacheDebug gameEvaluatorCacheDebug;
    private EvaluatorCache gameEvaluatorCache;
    private GameEvaluatorCacheComparator gameEvaluatorCacheComparator;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withMvvLva;

    public MoveSorterQuiescenceBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter(move -> !move.isQuiet());
    }

    public MoveSorterQuiescenceBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public MoveSorterQuiescenceBuilder withGameEvaluatorCache(EvaluatorCache gameEvaluatorCache) {
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
            gameEvaluatorCacheDebug = new EvaluatorCacheDebug();
            gameEvaluatorCacheDebug.setEvaluatorCacheRead(gameEvaluatorCache);
        }

        if (gameEvaluatorCache != null) {
            gameEvaluatorCacheComparator = new GameEvaluatorCacheComparator();
            if (withDebugSearchTree) {
                gameEvaluatorCacheComparator.setEvaluatorCacheRead(gameEvaluatorCacheDebug);
            } else {
                gameEvaluatorCacheComparator.setEvaluatorCacheRead(gameEvaluatorCache);
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
        searchListenerMediator.add(nodeMoveSorter);

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
            searchListenerMediator.addAcceptor(moveSorterDebug);
        }

        if (gameEvaluatorCacheDebug != null) {
            searchListenerMediator.addAcceptor(gameEvaluatorCacheDebug);
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

            switch (currentComparator) {
                case TranspositionHeadMoveComparator headMoveComparator ->
                        transpositionHeadMoveComparator.setNext(next);
                case TranspositionTailMoveComparator tailMoveComparator ->
                        transpositionTailMoveComparator.setNext(next);
                case RecaptureMoveComparator moveComparator -> recaptureMoveComparator.setNext(next);
                case GameEvaluatorCacheComparator evaluatorCacheComparator ->
                        gameEvaluatorCacheComparator.setNext(next);
                case MvvLvaComparator lvaComparator -> mvvLvaComparator.setNext(next);
                case PromotionComparator comparator -> promotionComparator.setNext(next);
                case PrincipalVariationComparator variationComparator -> principalVariationComparator.setNext(next);
                case null, default -> throw new RuntimeException("Unknow MoveComparator");
            }
        }


        return chain.getFirst();
    }
}
