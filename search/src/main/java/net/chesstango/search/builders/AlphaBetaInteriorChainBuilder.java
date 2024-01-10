package net.chesstango.search.builders;


import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaInteriorChainBuilder {
    private final AlphaBeta alphaBeta;
    private MoveSorter moveSorter;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private DebugFilter debugFilter;
    private SmartListenerMediator smartListenerMediator;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public AlphaBetaInteriorChainBuilder() {
        alphaBeta = new AlphaBeta();

        moveSorter = new DefaultMoveSorter();
    }

    public AlphaBetaInteriorChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }


    public AlphaBetaInteriorChainBuilder withTranspositionMoveSorter() {
        if (transpositionTable == null) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        moveSorter = new TranspositionMoveSorter();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withTriangularPV() {
        throw new RuntimeException("Please review implementation");
    }

    public AlphaBetaInteriorChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    /**
     * @return
     */
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
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.SearchNodeType.INTERIOR);
        }

        alphaBeta.setMoveSorter(moveSorter);
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(moveSorter);
        smartListenerMediator.add(alphaBeta);

        if (withStatistics) {
            smartListenerMediator.add(alphaBetaStatisticsExpected);
            smartListenerMediator.add(alphaBetaStatisticsVisited);
        }
        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }
        if (transpositionTable != null) {
            smartListenerMediator.add(transpositionTable);
        }
        if (debugFilter != null) {
            smartListenerMediator.add(debugFilter);
        }
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        if (alphaBetaStatisticsExpected != null) {
            chain.add(alphaBetaStatisticsExpected);
        }

        chain.add(alphaBeta);

        if (alphaBetaStatisticsVisited != null) {
            chain.add(alphaBetaStatisticsVisited);
        }

        chain.add(alphaBetaFlowControl);


        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTable) {
                transpositionTable.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsExpected) {
                alphaBetaStatisticsExpected.setNext(next);
            } else if (currentFilter instanceof AlphaBeta) {
                alphaBeta.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugFilter.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }


        return chain.get(0);
    }
}
