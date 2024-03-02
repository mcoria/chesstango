package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluatorCacheRead;
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
public class MoveSorterBuilder {
    private final NodeMoveSorter nodeMoveSorter;
    private final QuietComparator quietComparator;
    private final RecaptureMoveComparator recaptureMoveComparator;
    private final DefaultMoveComparator defaultMoveComparator;
    private SmartListenerMediator smartListenerMediator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparatorQ;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparatorQ;
    private TrapMoveSorter trapMoveSorter;
    private TrapReadFromCache trapReadFromCache;
    private GameEvaluatorCacheRead gameEvaluatorCacheRead;
    private GameEvaluatorComparator gameEvaluatorComparator;
    private KillerMoveComparator killerMoveComparator;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;

    public MoveSorterBuilder() {
        this.nodeMoveSorter = new NodeMoveSorter();
        this.quietComparator = new QuietComparator();
        this.recaptureMoveComparator = new RecaptureMoveComparator();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }


    public MoveSorterBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public MoveSorterBuilder withGameEvaluatorCache(GameEvaluatorCacheRead gameEvaluatorCacheRead) {
        this.gameEvaluatorCacheRead = gameEvaluatorCacheRead;
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
        if (withTranspositionTable) {
            transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
            transpositionHeadMoveComparatorQ = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);

            transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
            transpositionTailMoveComparatorQ = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
        }

        if (withDebugSearchTree) {
            trapMoveSorter = new TrapMoveSorter();
            trapReadFromCache = new TrapReadFromCache();
            trapReadFromCache.setGameEvaluatorCacheRead(gameEvaluatorCacheRead);
        }

        if (gameEvaluatorCacheRead != null) {
            gameEvaluatorComparator = new GameEvaluatorComparator();
            if (withDebugSearchTree) {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(trapReadFromCache);
            } else {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(gameEvaluatorCacheRead);
            }
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

        if (transpositionHeadMoveComparatorQ != null) {
            smartListenerMediator.add(transpositionHeadMoveComparatorQ);
        }

        if (transpositionTailMoveComparator != null) {
            smartListenerMediator.add(transpositionTailMoveComparator);
        }

        if (transpositionTailMoveComparatorQ != null) {
            smartListenerMediator.add(transpositionTailMoveComparatorQ);
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

        /*
        if (quietComparator != null) {
            smartListenerMediator.add(quietComparator);
        }
         */
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);

            chain.add(transpositionHeadMoveComparatorQ);
            chain.add(transpositionTailMoveComparatorQ);
        }

        if (gameEvaluatorComparator != null) {
            chain.add(gameEvaluatorComparator);
        }

        chain.add(quietComparator);

        chain.add(recaptureMoveComparator);

        if (killerMoveComparator != null) {
            chain.add(killerMoveComparator);
        }

        chain.add(defaultMoveComparator);

        for (int i = 0; i < chain.size() - 1; i++) {
            MoveComparator currentComparator = chain.get(i);
            MoveComparator next = chain.get(i + 1);

            if (currentComparator instanceof TranspositionHeadMoveComparator && currentComparator == transpositionHeadMoveComparator) {
                transpositionHeadMoveComparator.setNext(next);
            } else if (currentComparator instanceof TranspositionTailMoveComparator && currentComparator == transpositionTailMoveComparator) {
                transpositionTailMoveComparator.setNext(next);
            } else if (currentComparator instanceof TranspositionHeadMoveComparator && currentComparator == transpositionHeadMoveComparatorQ) {
                transpositionHeadMoveComparatorQ.setNext(next);
            } else if (currentComparator instanceof TranspositionTailMoveComparator && currentComparator == transpositionTailMoveComparatorQ) {
                transpositionTailMoveComparatorQ.setNext(next);
            } else if (currentComparator instanceof RecaptureMoveComparator) {
                recaptureMoveComparator.setNext(next);
            } else if (currentComparator instanceof GameEvaluatorComparator) {
                gameEvaluatorComparator.setNext(next);
            } else if (currentComparator instanceof KillerMoveComparator) {
                killerMoveComparator.setNext(next);
            } else if (currentComparator instanceof QuietComparator) {
                quietComparator.setNext(next);
            } else {
                throw new RuntimeException("Unknow MoveComparator");
            }
        }


        return chain.get(0);
    }
}
