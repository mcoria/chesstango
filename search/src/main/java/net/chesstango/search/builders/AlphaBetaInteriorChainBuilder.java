package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaInteriorChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterBuilder moveSorterBuilder;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private DebugFilter debugFilter;
    private TriangularPV triangularPV;
    private KillerMoveTracker killerMoveTracker;
    private SmartListenerMediator smartListenerMediator;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;
    private boolean withKillerMoveSorter;

    public AlphaBetaInteriorChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterBuilder = new MoveSorterBuilder();
    }

    public AlphaBetaInteriorChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.moveSorterBuilder.withSmartListenerMediator(smartListenerMediator);
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        moveSorterBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withGameEvaluatorCache(GameEvaluatorCache gameEvaluatorCache) {
        moveSorterBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        return this;
    }

    public AlphaBetaInteriorChainBuilder withKillerMoveSorter() {
        withKillerMoveSorter = true;
        moveSorterBuilder.withKillerMoveSorter();
        return this;
    }


    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        alphaBeta.setMoveSorter(moveSorterBuilder.build());

        return createChain();
    }

    private void buildObjects() {
        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }
        if (withTranspositionTable) {
            transpositionTable = new TranspositionTable();
        }
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.INTERIOR);
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
        if (withKillerMoveSorter) {
            killerMoveTracker = new KillerMoveTracker();
        }
    }

    private void setupListenerMediator() {
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
        if (triangularPV != null) {
            smartListenerMediator.add(triangularPV);
        }
        if (killerMoveTracker != null) {
            smartListenerMediator.add(killerMoveTracker);
        }


        smartListenerMediator.add(alphaBeta);
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

        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        if (killerMoveTracker != null) {
            chain.add(killerMoveTracker);
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
            } else if (currentFilter instanceof TriangularPV) {
                triangularPV.setNext(next);
            } else if (currentFilter instanceof KillerMoveTracker) {
                killerMoveTracker.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
