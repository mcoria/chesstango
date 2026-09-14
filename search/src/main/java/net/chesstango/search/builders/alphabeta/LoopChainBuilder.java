package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.alphabeta.evaluator.filters.LoopEvaluation;
import net.chesstango.search.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.alphabeta.statistics.node.filters.LoopNodeStatistics;
import net.chesstango.search.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LoopChainBuilder extends AbstractChainBuilder {
    private final LoopEvaluation loopEvaluation;
    private ZobristTracker zobristTracker;
    private LoopNodeStatistics loopNodeStatistics;
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

    public LoopChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }


    @Override
    protected  void buildObjects() {
        extendPV = new ExtendPV();

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            loopNodeStatistics = new LoopNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.LOOP);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }

    }

    @Override
    protected  void setupListenerMediator() {
        if (zobristTracker != null) {
            listenerMediator.add(zobristTracker);
        }

        if (loopNodeStatistics != null) {
            listenerMediator.add(loopNodeStatistics);
        }

        if (debugFilter != null) {
            listenerMediator.add(debugFilter);
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

        if (loopNodeStatistics != null) {
            chain.add(loopNodeStatistics);
        }

        chain.add(loopEvaluation);

        return createChain(chain);
    }

}

