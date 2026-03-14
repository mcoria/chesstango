package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.builders.MoveSorterBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaInteriorChainBuilder extends AbstractChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterBuilder moveSorterBuilder;
    private AlphaBetaInteriorNodeStatistics alphaBetaNodeStatistics;
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
            alphaBetaNodeStatistics = new AlphaBetaInteriorNodeStatistics();
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
            searchListenerMediator.add(alphaBetaNodeStatistics);
        }
        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }
        if (transpositionTable != null) {
            searchListenerMediator.add(transpositionTable);
        }
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
        if (triangularPV != null) {
            searchListenerMediator.add(triangularPV);
        }
        if (killerMoveTracker != null) {
            searchListenerMediator.add(killerMoveTracker);
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

        if (alphaBetaNodeStatistics != null) {
            chain.add(alphaBetaNodeStatistics);
        }

        chain.add(alphaBeta);


        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        if (killerMoveTracker != null) {
            chain.add(killerMoveTracker);
        }

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
