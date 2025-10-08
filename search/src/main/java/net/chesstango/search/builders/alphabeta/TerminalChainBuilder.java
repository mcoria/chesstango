package net.chesstango.search.builders.alphabeta;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableTerminal;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TerminalChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private Evaluator evaluator;
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

    public TerminalChainBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
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
        alphaBetaEvaluation.setEvaluator(evaluator);


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
        if (zobristTracker != null) {
            searchListenerMediator.addAcceptor(zobristTracker);
        }
        if (debugFilter != null) {
            searchListenerMediator.addAcceptor(debugFilter);
        }
        if (transpositionTableTerminal != null) {
            searchListenerMediator.addAcceptor(transpositionTableTerminal);
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

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case ZobristTracker tracker -> zobristTracker.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case TranspositionTableTerminal tableTerminal -> transpositionTableTerminal.setNext(next);
                case AlphaBetaEvaluation betaEvaluation -> {
                    //alphaBetaEvaluation.setNext(next);
                }
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
