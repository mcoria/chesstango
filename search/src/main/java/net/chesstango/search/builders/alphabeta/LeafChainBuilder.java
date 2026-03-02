package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableLeaf;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LeafChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private ZobristTracker zobristTracker;
    private DebugFilter debugSearchTree;
    private TranspositionTableLeaf transpositionTable;
    private SearchListenerMediator searchListenerMediator;

    private boolean withZobristTracker;
    private boolean withDebugSearchTree;
    private boolean withTranspositionTable;


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

    public LeafChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
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
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(DebugNode.NodeTopology.LEAF);
        }
        if (withTranspositionTable) {
            transpositionTable = new TranspositionTableLeaf();
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(leaf);

        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }
        if (debugSearchTree != null) {
            searchListenerMediator.add(debugSearchTree);
        }
        if (transpositionTable != null) {
            searchListenerMediator.add(transpositionTable);
        }
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugSearchTree != null) {
            chain.add(debugSearchTree);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        chain.add(leaf);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case ZobristTracker zobristTrackerFilter -> zobristTrackerFilter.setNext(next);
                case DebugFilter debugFilter -> debugFilter.setNext(next);
                case TranspositionTableLeaf transpositionTableFilter -> transpositionTableFilter.setNext(next);
                case AlphaBetaEvaluation alphaBetaEvaluation -> {
                    //leaf
                }
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
