package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.TrapMoveSorter;
import net.chesstango.search.smart.alphabeta.debug.TrapReadFromCache;
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
    private TrapMoveSorter trapMoveSorter;
    private TrapReadFromCache trapReadFromCache;
    private GameEvaluatorCache gameEvaluatorCache;
    private GameEvaluatorComparator gameEvaluatorComparator;
    private KillerMoveComparator killerMoveComparator;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withRecaptureSorter;
    private boolean withKillerMoveSorter;

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


    public MoveSorterQuiescenceBuilder withKillerMoveSorter() {
        this.withKillerMoveSorter = true;
        return this;
    }


    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();


        MoveSorter moveSorter = nodeMoveSorter;
        nodeMoveSorter.setMoveComparator(createComparatorChain());

        if (trapMoveSorter != null) {
            trapMoveSorter.setMoveSorterImp(moveSorter);
            moveSorter = trapMoveSorter;
        }

        return moveSorter;
    }

    private void buildObjects() {
        defaultMoveComparator = new DefaultMoveComparator();

        if (withTranspositionTable) {
            transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
            transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
        }

        if (withDebugSearchTree) {
            trapMoveSorter = new TrapMoveSorter();
            trapReadFromCache = new TrapReadFromCache();
            trapReadFromCache.setGameEvaluatorCacheRead(gameEvaluatorCache);
        }

        if (gameEvaluatorCache != null) {
            gameEvaluatorComparator = new GameEvaluatorComparator();
            if (withDebugSearchTree) {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(trapReadFromCache);
            } else {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(gameEvaluatorCache);
            }
        }

        if (withRecaptureSorter) {
            recaptureMoveComparator = new RecaptureMoveComparator();
        }

        if (withKillerMoveSorter) {
            killerMoveComparator = new KillerMoveComparator();
        }
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(nodeMoveSorter);

        if (transpositionHeadMoveComparator != null) {
            smartListenerMediator.add(transpositionHeadMoveComparator);
        }

        if (transpositionTailMoveComparator != null) {
            smartListenerMediator.add(transpositionTailMoveComparator);
        }

        if (recaptureMoveComparator != null) {
            smartListenerMediator.add(recaptureMoveComparator);
        }

        if (gameEvaluatorComparator != null) {
            smartListenerMediator.add(gameEvaluatorComparator);
        }

        if (trapMoveSorter != null) {
            smartListenerMediator.add(trapMoveSorter);
        }

        if (trapReadFromCache != null) {
            smartListenerMediator.add(trapReadFromCache);
        }

        if (killerMoveComparator != null) {
            smartListenerMediator.add(killerMoveComparator);
        }
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        if (recaptureMoveComparator != null) {
            chain.add(recaptureMoveComparator);
        }

        /**
         * Todavia NO CAPTURARMOS Killer Move en Quiescence search
         */
        /*
        if (killerMoveComparator != null) {
            chain.add(killerMoveComparator);
        }
         */

        if (gameEvaluatorComparator != null) {
            chain.add(gameEvaluatorComparator);
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
            } else if (currentComparator instanceof GameEvaluatorComparator) {
                gameEvaluatorComparator.setNext(next);
            } else {
                throw new RuntimeException("Unknow MoveComparator");
            }
        }


        return chain.get(0);
    }
}
