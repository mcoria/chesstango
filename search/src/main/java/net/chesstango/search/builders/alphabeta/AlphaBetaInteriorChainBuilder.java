package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.sorters.MoveSorterInteriorBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.pv.filters.ClearPV;
import net.chesstango.search.smart.alphabeta.pv.filters.UpdatePV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeVisited;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaInteriorChainBuilder extends AbstractChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterInteriorBuilder moveSorterBuilder;
    private AlphaBetaInteriorNodeVisited alphaBetaInteriorNodeVisited;
    private AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private DebugFilter debugFilter;
    private ClearPV clearPV;
    private UpdatePV updatePV;
    private KillerMoveTracker killerMoveTracker;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withKillerMoveSorter;

    public AlphaBetaInteriorChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterBuilder = new MoveSorterInteriorBuilder();
    }

    public AlphaBetaInteriorChainBuilder withIterativeDeepening() {
        moveSorterBuilder.withIterativeDeepening();
        return this;
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

    public AlphaBetaInteriorChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public AlphaBetaInteriorChainBuilder withGameEvaluatorCache() {
        moveSorterBuilder.withGameEvaluatorCache();
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

    @Override
    protected void buildObjects() {
        clearPV = new ClearPV();
        updatePV = new UpdatePV();

        if (withStatistics) {
            alphaBetaInteriorNodeVisited = new AlphaBetaInteriorNodeVisited();
            alphaBetaInteriorNodeExpected = new AlphaBetaInteriorNodeExpected();
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

        if (withKillerMoveSorter) {
            killerMoveTracker = new KillerMoveTracker();
        }
    }

    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(alphaBeta);

        if (alphaBetaInteriorNodeVisited != null) {
            searchListenerMediator.add(alphaBetaInteriorNodeVisited);
        }

        if (alphaBetaInteriorNodeExpected != null) {
            searchListenerMediator.add(alphaBetaInteriorNodeExpected);
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

        if (clearPV != null) {
            searchListenerMediator.add(clearPV);
        }

        if (updatePV != null) {
            searchListenerMediator.add(updatePV);
        }

        if (killerMoveTracker != null) {
            searchListenerMediator.add(killerMoveTracker);
        }
    }

    @Override
    protected void linkObjects() {
        alphaBeta.setMoveSorter(moveSorterBuilder.build());
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (clearPV != null) {
            chain.add(clearPV);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (alphaBetaInteriorNodeVisited != null) {
            chain.add(alphaBetaInteriorNodeVisited);
        }

        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        // Debe ir despues de TT para que contabilice expected correctamente
        if (alphaBetaInteriorNodeExpected != null) {
            chain.add(alphaBetaInteriorNodeExpected);
        }

        chain.add(alphaBeta);

        if (updatePV != null) {
            chain.add(updatePV);
        }

        if (killerMoveTracker != null) {
            chain.add(killerMoveTracker);
        }

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
