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
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private final MoveEvaluationTracker moveEvaluationTracker;
    private AlphaBetaFilter terminal;
    private AlphaBetaFilter interior;
    private AlphaBetaFilter horizon;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private SmartListenerMediator smartListenerMediator;
    private ZobristTracker zobristTracker;


    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;

    public AlphaBetaRootChainBuilder() {
        alphaBetaRoot = new AlphaBetaRoot();

        moveEvaluationTracker = new MoveEvaluationTracker();

        alphaBetaFlowControl = new AlphaBetaFlowControl();
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


    public AlphaBetaRootChainBuilder withInterior(AlphaBetaFilter alphaBeta) {
        this.interior = alphaBeta;
        return this;
    }

    public AlphaBetaRootChainBuilder withHorizon(AlphaBetaFilter horizon) {
        this.horizon = horizon;
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

    public AlphaBetaRootChainBuilder withTerminal(AlphaBetaFilter terminal) {
        this.terminal = terminal;
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
            aspirationWindows.setMoveEvaluationTracker(moveEvaluationTracker);
        }

        if (withTranspositionTable) {
            transpositionTableRoot = new TranspositionTableRoot();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        moveEvaluationTracker.setStopProcessingCatch(stopProcessingCatch);
    }


    private void setupListenerMediator() {
        smartListenerMediator.add(alphaBetaRoot);
        smartListenerMediator.add(moveEvaluationTracker);
        smartListenerMediator.add(alphaBetaFlowControl);


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
        chain.add(moveEvaluationTracker);

        if (alphaBetaStatisticsVisited != null) {
            chain.add(alphaBetaStatisticsVisited);
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
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        alphaBetaFlowControl.setInteriorNode(interior);
        alphaBetaFlowControl.setHorizonNode(horizon);
        alphaBetaFlowControl.setTerminalNode(terminal);

        return chain.get(0);
    }
}
