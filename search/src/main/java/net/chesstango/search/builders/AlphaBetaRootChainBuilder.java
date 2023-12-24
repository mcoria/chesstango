package net.chesstango.search.builders;


import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootChainBuilder {
    private final AlphaBetaRoot alphaBetaRoot;
    private final MoveEvaluationTracker moveEvaluationTracker;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private SmartListenerMediator smartListenerMediator;
    private ZobristTracker zobristTracker;
    private DebugTree debugTree;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    private AlphaBetaFilter alphaBetaFlowControl;

    public AlphaBetaRootChainBuilder() {
        alphaBetaRoot = new AlphaBetaRoot();

        moveEvaluationTracker = new MoveEvaluationTracker();
    }


    public AlphaBetaRootChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaRootChainBuilder withStopProcessingCatch() {
        stopProcessingCatch = new StopProcessingCatch();
        return this;
    }


    public AlphaBetaRootChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public AlphaBetaRootChainBuilder withAspirationWindows() {
        this.withAspirationWindows = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withTriangularPV() {
        throw new RuntimeException("Please review implementation");
    }

    public AlphaBetaRootChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withDebugSearchTree() {
        this.debugTree = new DebugTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
            aspirationWindows.setSmartListenerMediator(smartListenerMediator);
        }

        if (withTranspositionTable) {
            transpositionTableRoot = new TranspositionTableRoot();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugTree = new DebugTree();
        }

        moveEvaluationTracker.setStopProcessingCatch(stopProcessingCatch);
    }


    private void setupListenerMediator() {
        smartListenerMediator.add(alphaBetaRoot);
        smartListenerMediator.add(moveEvaluationTracker);


        if (withStatistics) {
            smartListenerMediator.add(alphaBetaStatisticsExpected);
            smartListenerMediator.add(alphaBetaStatisticsVisited);
        }

        if (aspirationWindows != null) {
            smartListenerMediator.add(aspirationWindows);
        }

        if (stopProcessingCatch != null) {
            smartListenerMediator.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }

        if (transpositionTableRoot != null) {
            smartListenerMediator.add(transpositionTableRoot);
        }

        if (debugTree != null) {
            smartListenerMediator.add(debugTree);
        }
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (stopProcessingCatch != null) {
            chain.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (transpositionTableRoot != null) {
            chain.add(transpositionTableRoot);
        }

        if (aspirationWindows != null) {
            chain.add(aspirationWindows);
        }

        if (alphaBetaStatisticsExpected != null) {
            chain.add(alphaBetaStatisticsExpected);
        }

        chain.add(alphaBetaRoot);

        if (alphaBetaStatisticsVisited != null) {
            chain.add(alphaBetaStatisticsVisited);
        }

        chain.add(moveEvaluationTracker);

        if (debugTree != null) {
            chain.add(debugTree);
        }

        chain.add(alphaBetaFlowControl);


        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof StopProcessingCatch) {
                stopProcessingCatch.setNext(next);
            } else if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableRoot) {
                transpositionTableRoot.setNext(next);
            } else if (currentFilter instanceof AspirationWindows) {
                aspirationWindows.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsExpected) {
                alphaBetaStatisticsExpected.setNext(next);
            } else if (currentFilter instanceof AlphaBetaRoot) {
                alphaBetaRoot.setNext(next);
            } else if (currentFilter instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(next);
            } else if (currentFilter instanceof DebugTree) {
                debugTree.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
