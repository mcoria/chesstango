package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

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
    private SearchListenerMediator searchListenerMediator;
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

    public AlphaBetaInteriorChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        this.searchListenerMediator = searchListenerMediator;
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

    public AlphaBetaInteriorChainBuilder withGameEvaluatorCache(EvaluatorCache gameEvaluatorCache) {
        moveSorterBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        return this;
    }

    public AlphaBetaInteriorChainBuilder withKillerMoveSorter() {
        withKillerMoveSorter = true;
        moveSorterBuilder.withKillerMoveSorter();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withRecaptureSorter() {
        moveSorterBuilder.withKillerMoveSorter();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withMvvLvaSorter() {
        moveSorterBuilder.withMvvLva();
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
            searchListenerMediator.addAcceptor(alphaBetaStatisticsExpected);
            searchListenerMediator.addAcceptor(alphaBetaStatisticsVisited);
        }
        if (zobristTracker != null) {
            searchListenerMediator.addAcceptor(zobristTracker);
        }
        if (transpositionTable != null) {
            searchListenerMediator.add(transpositionTable);
        }
        if (debugFilter != null) {
            searchListenerMediator.addAcceptor(debugFilter);
        }
        if (triangularPV != null) {
            searchListenerMediator.add(triangularPV);
        }
        if (killerMoveTracker != null) {
            searchListenerMediator.addAcceptor(killerMoveTracker);
        }


        searchListenerMediator.add(alphaBeta);
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

            switch (currentFilter) {
                case ZobristTracker tracker -> zobristTracker.setNext(next);
                case TranspositionTable table -> transpositionTable.setNext(next);
                case AlphaBetaStatisticsExpected betaStatisticsExpected -> alphaBetaStatisticsExpected.setNext(next);
                case AlphaBeta beta -> alphaBeta.setNext(next);
                case AlphaBetaStatisticsVisited betaStatisticsVisited -> alphaBetaStatisticsVisited.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case TriangularPV pv -> triangularPV.setNext(next);
                case KillerMoveTracker moveTracker -> killerMoveTracker.setNext(next);
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
