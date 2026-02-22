package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.evaluator.filters.LoopEvaluation;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LoopChainBuilder {
    private final LoopEvaluation loopEvaluation;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private SearchListenerMediator searchListenerMediator;

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

    public void withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
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
            searchListenerMediator.addAcceptor(zobristTracker);
        }

        if (debugFilter != null) {
            searchListenerMediator.addAcceptor(debugFilter);
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

            switch (currentFilter) {
                case ZobristTracker tracker -> zobristTracker.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case LoopEvaluation evaluation -> {
                }
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }

}

