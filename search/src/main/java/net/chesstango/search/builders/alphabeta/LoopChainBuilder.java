package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.LoopEvaluation;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LoopChainBuilder {
    private final LoopEvaluation loopEvaluation;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private SmartListenerMediator smartListenerMediator;

    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public LoopChainBuilder() {
        loopEvaluation = new LoopEvaluation();
    }

    public LoopChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public LoopChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public void withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            this.debugFilter = new DebugFilter(DebugNode.NodeTopology.LOOP);
        }

    }

    private void setupListenerMediator() {
        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
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

        chain.add(loopEvaluation);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugFilter.setNext(next);
            } else if (currentFilter instanceof LoopEvaluation) {

            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }

}

