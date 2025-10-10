package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTable;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaHorizonChainBuilder {
    private SearchListenerMediator searchListenerMediator;
    private AlphaBetaFilter quiescence;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;

    public AlphaBetaHorizonChainBuilder() {
    }

    public AlphaBetaHorizonChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }


    public AlphaBetaHorizonChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withGameEvaluator(Evaluator evaluator) {
        return this;
    }

    public AlphaBetaHorizonChainBuilder withExtension(AlphaBetaFilter quiescenceChain) {
        this.quiescence = quiescenceChain;
        return this;
    }

    public AlphaBetaHorizonChainBuilder withDebugSearchTree() {
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
            zobristTracker = new ZobristTracker();
        }

        if (withTranspositionTable) {
            transpositionTable = new TranspositionTable();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.HORIZON);
        }
    }

    private void setupListenerMediator() {
        if (debugFilter != null) {
            searchListenerMediator.addAcceptor(debugFilter);
        }
        if (zobristTracker != null) {
            searchListenerMediator.addAcceptor(zobristTracker);
        }
        if (transpositionTable != null) {
            searchListenerMediator.addAcceptor(transpositionTable);
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

        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        chain.add(quiescence);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case ZobristTracker tracker -> zobristTracker.setNext(next);
                case TranspositionTable table -> transpositionTable.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
