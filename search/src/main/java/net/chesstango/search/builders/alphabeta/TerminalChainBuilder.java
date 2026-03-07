package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableTerminal;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TerminalChainBuilder extends AbstractChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private TranspositionTableTerminal transpositionTableTerminal;
    private SearchListenerMediator searchListenerMediator;

    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    private boolean withTranspositionTable;

    public TerminalChainBuilder() {
        alphaBetaEvaluation = new AlphaBetaEvaluation();
    }


    public TerminalChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public TerminalChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public TerminalChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public TerminalChainBuilder withTranspositionTable() {
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
            debugFilter = new DebugFilter(DebugNode.NodeTopology.TERMINAL);
        }

        if (withTranspositionTable) {
            transpositionTableTerminal = new TranspositionTableTerminal();
        }
    }

    private void setupListenerMediator() {
        searchListenerMediator.add(alphaBetaEvaluation);

        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
        if (transpositionTableTerminal != null) {
            searchListenerMediator.add(transpositionTableTerminal);
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

        if (transpositionTableTerminal != null) {
            chain.add(transpositionTableTerminal);
        }

        chain.add(alphaBetaEvaluation);

        return createChain(chain);
    }
}
