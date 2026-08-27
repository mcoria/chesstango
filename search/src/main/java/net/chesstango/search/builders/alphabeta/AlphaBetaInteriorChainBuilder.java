package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.sorters.MoveSorterInteriorBuilder;
import net.chesstango.search.SearchListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaInteriorNodeExpected;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaInteriorNodeVisited;
import net.chesstango.search.smart.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;
import net.chesstango.search.sorters.MoveSorter;

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
    private ExtendPV extendPV;
    private PropagatePV propagatePV;
    private KillerMoveTracker killerMoveTracker;
    private MoveSorter moveSorter;

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
        moveSorterBuilder.withKillerMove();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withRecaptureSorter() {
        moveSorterBuilder.withRecapture();
        return this;
    }

    public AlphaBetaInteriorChainBuilder withMvvLvaSorter() {
        moveSorterBuilder.withMvvLva();
        return this;
    }

    @Override
    protected void buildObjects() {
        extendPV = new ExtendPV();
        propagatePV = new PropagatePV();

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
            debugFilter = new DebugFilter(NodeTopology.INTERIOR);
        }

        if (withKillerMoveSorter) {
            killerMoveTracker = new KillerMoveTracker();
        }

        moveSorter = moveSorterBuilder.build();
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

        if (extendPV != null) {
            searchListenerMediator.add(extendPV);
        }

        if (propagatePV != null) {
            searchListenerMediator.add(propagatePV);
        }

        if (killerMoveTracker != null) {
            searchListenerMediator.add(killerMoveTracker);
        }
    }

    @Override
    public void link() {
        alphaBeta.setMoveSorter(moveSorter);
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (extendPV != null) {
            chain.add(extendPV);
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

        if (propagatePV != null) {
            chain.add(propagatePV);
        }

        if (killerMoveTracker != null) {
            chain.add(killerMoveTracker);
        }

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
