package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.sorters.MoveSorterQuiescenceBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.smart.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.smart.alphabeta.pv.filters.PropagatePV;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaQuiescenceNodeExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaQuiescenceNodeVisited;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceChainBuilder extends AbstractChainBuilder {
    private final Quiescence quiescence;
    private final MoveSorterQuiescenceBuilder moveSorterBuilder;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private AlphaBetaQuiescenceNodeVisited alphaBetaQuiescenceNodeVisited;
    private AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private PropagatePV propagatePV;
    private ExtendPV extendPV;
    private MoveSorter moveSorter;

    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;


    public QuiescenceChainBuilder() {
        quiescence = new Quiescence();
        moveSorterBuilder = new MoveSorterQuiescenceBuilder();
    }

    public QuiescenceChainBuilder withIterativeDeepening() {
        moveSorterBuilder.withIterativeDeepening();
        return this;
    }

    public QuiescenceChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public QuiescenceChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        return this;
    }

    public QuiescenceChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        moveSorterBuilder.withTranspositionTable();
        return this;
    }

    public QuiescenceChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public QuiescenceChainBuilder withGameEvaluatorCache() {
        moveSorterBuilder.withGameEvaluatorCache();
        return this;
    }

    public QuiescenceChainBuilder withRecaptureSorter() {
        moveSorterBuilder.withRecaptureSorter();
        return this;
    }

    public QuiescenceChainBuilder withMvvLvaSorter() {
        moveSorterBuilder.withMvvLva();
        return this;
    }

    @Override
    protected void buildObjects() {
        extendPV = new ExtendPV();
        propagatePV = new PropagatePV();

        if (withStatistics) {
            alphaBetaQuiescenceNodeVisited = new AlphaBetaQuiescenceNodeVisited();
            alphaBetaQuiescenceNodeExpected = new AlphaBetaQuiescenceNodeExpected();
        }

        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }

        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.QUIESCENCE);
        }

        moveSorter = moveSorterBuilder.build();
    }

    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(quiescence);

        if (alphaBetaQuiescenceNodeVisited != null) {
            searchListenerMediator.add(alphaBetaQuiescenceNodeVisited);
        }

        if (alphaBetaQuiescenceNodeExpected != null) {
            searchListenerMediator.add(alphaBetaQuiescenceNodeExpected);
        }

        if (zobristQTracker != null) {
            searchListenerMediator.add(zobristQTracker);
        }

        if (transpositionTableQ != null) {
            searchListenerMediator.add(transpositionTableQ);
        }

        if (withDebugSearchTree) {
            searchListenerMediator.add(debugFilter);
        }

        if (extendPV != null) {
            searchListenerMediator.add(extendPV);
        }

        if (propagatePV != null) {
            searchListenerMediator.add(propagatePV);
        }
    }

    @Override
    public void link() {
        quiescence.setMoveSorter(moveSorter);
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

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        if (alphaBetaQuiescenceNodeVisited != null) {
            chain.add(alphaBetaQuiescenceNodeVisited);
        }

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        if (alphaBetaQuiescenceNodeExpected != null) {
            chain.add(alphaBetaQuiescenceNodeExpected);
        }

        chain.add(quiescence);

        if (propagatePV != null) {
            chain.add(propagatePV);
        }

        chain.add(alphaBetaFlowControl);


        return createChain(chain);
    }

}
