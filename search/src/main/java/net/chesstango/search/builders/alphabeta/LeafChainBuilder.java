package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LeafChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugSearchTree;
    private SearchListenerMediator searchListenerMediator;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public LeafChainBuilder() {
        leaf = new AlphaBetaEvaluation();
    }

    public LeafChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public LeafChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public LeafChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
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
            zobristQTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(DebugNode.NodeTopology.LEAF);
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.addAcceptor(leaf);

        if (zobristQTracker != null) {
            searchListenerMediator.addAcceptor(zobristQTracker);
        }
        if (debugSearchTree != null) {
            searchListenerMediator.addAcceptor(debugSearchTree);
        }
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugSearchTree != null) {
            chain.add(debugSearchTree);
        }

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        chain.add(leaf);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case ZobristTracker zobristTracker -> zobristQTracker.setNext(next);
                case DebugFilter debugFilter -> debugSearchTree.setNext(next);
                case AlphaBetaEvaluation alphaBetaEvaluation -> {
                    //leaf
                }
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
