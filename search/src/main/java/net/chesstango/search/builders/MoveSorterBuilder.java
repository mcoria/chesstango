package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugEvaluation;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MoveSorterBuilder {
    private SmartListenerMediator smartListenerMediator;
    private NodeMoveSorter nodeMoveSorter;
    private DefaultMoveComparator defaultMoveComparator;
    private RecaptureMoveComparator recaptureMoveComparator;
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;
    private TranspositionTailMoveComparator transpositionTailMoveComparator;
    private DebugSorter debugSorter;
    private DebugEvaluation debugEvaluation;
    private GameEvaluatorCache gameEvaluatorCache;
    private GameEvaluatorComparator gameEvaluatorComparator;
    private boolean withQuietFilter;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;

    public MoveSorterBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public MoveSorterBuilder withGameEvaluatorCache(GameEvaluatorCache gameEvaluatorCache) {
        this.gameEvaluatorCache = gameEvaluatorCache;
        return this;
    }

    public MoveSorterBuilder withMoveQuietFilter() {
        this.withQuietFilter = true;
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


    public MoveSorter build() {
        buildObjects();

        setupListenerMediator();


        MoveSorter moveSorter = nodeMoveSorter;
        nodeMoveSorter.setMoveComparator(createComparatorChain());

        if (debugSorter != null) {
            debugSorter.setMoveSorterImp(moveSorter);
            moveSorter = debugSorter;
        }

        return moveSorter;
    }

    private void buildObjects() {
        nodeMoveSorter = withQuietFilter ? new NodeMoveSorter(move -> !move.isQuiet()) : new NodeMoveSorter();

        recaptureMoveComparator = new RecaptureMoveComparator();

        defaultMoveComparator = new DefaultMoveComparator();

        if (withTranspositionTable) {
            if (withQuietFilter) {
                transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
                transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getQMaxMap, SearchByCycleContext::getQMinMap);
            } else {
                transpositionHeadMoveComparator = new TranspositionHeadMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
                transpositionTailMoveComparator = new TranspositionTailMoveComparator(SearchByCycleContext::getMaxMap, SearchByCycleContext::getMinMap);
            }
        }

        if (withDebugSearchTree) {
            debugSorter = new DebugSorter();
            debugEvaluation = new DebugEvaluation();
            debugEvaluation.setGameEvaluatorCacheRead(gameEvaluatorCache);
        }

        if (gameEvaluatorCache != null) {
            gameEvaluatorComparator = new GameEvaluatorComparator();
            if (withDebugSearchTree) {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(debugEvaluation);
            } else {
                gameEvaluatorComparator.setGameEvaluatorCacheRead(gameEvaluatorCache);
            }
        }
    }

    private void setupListenerMediator() {
        if (nodeMoveSorter != null) {
            smartListenerMediator.add(nodeMoveSorter);
        }

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

        if (debugSorter != null) {
            smartListenerMediator.add(debugSorter);
        }

        if (debugEvaluation != null) {
            smartListenerMediator.add(debugEvaluation);
        }
    }


    private MoveComparator createComparatorChain() {
        List<MoveComparator> chain = new LinkedList<>();

        if (withTranspositionTable) {
            chain.add(transpositionHeadMoveComparator);
            chain.add(transpositionTailMoveComparator);
        }

        chain.add(recaptureMoveComparator);

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
