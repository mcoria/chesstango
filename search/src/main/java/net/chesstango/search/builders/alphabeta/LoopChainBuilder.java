package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.evaluator.filters.LoopEvaluation;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaLoopNodeStatistics;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LoopChainBuilder extends AbstractChainBuilder {
    private final LoopEvaluation loopEvaluation;
    private ZobristTracker zobristTracker;
    private AlphaBetaLoopNodeStatistics alphaBetaLoopNodeStatistics;
    private DebugFilter debugFilter;
    private ExtendPV extendPV;

    private boolean withZobristTracker;
    private boolean withStatistics;
    private boolean withDebugSearchTree;

    public LoopChainBuilder() {
        loopEvaluation = new LoopEvaluation();
    }

    public LoopChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public LoopChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public LoopChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public LoopChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }


    @Override
    protected  void buildObjects() {
        extendPV = new ExtendPV();

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            alphaBetaLoopNodeStatistics = new AlphaBetaLoopNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.LOOP);
        }

        if (extendPV != null) {
            searchListenerMediator.add(extendPV);
        }

    }

    @Override
    protected  void setupListenerMediator() {
        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }

        if (alphaBetaLoopNodeStatistics != null) {
            searchListenerMediator.add(alphaBetaLoopNodeStatistics);
        }

        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
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

        if (alphaBetaLoopNodeStatistics != null) {
            chain.add(alphaBetaLoopNodeStatistics);
        }

        chain.add(loopEvaluation);

        return createChain(chain);
    }

}

