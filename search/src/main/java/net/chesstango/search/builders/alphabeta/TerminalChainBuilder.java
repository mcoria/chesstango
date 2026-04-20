package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.pv.filters.ClearPV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaTerminalNodeStatistics;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TerminalChainBuilder extends AbstractChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private ZobristTracker zobristTracker;
    private AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics;
    private DebugFilter debugFilter;
    private ClearPV clearPV;


    /**
     * TranspositionTableTerminal escribe demasiadas entradas en TT y sobreescribe aquellas entradas que si interesan
     */
    //private TranspositionTableTerminal transpositionTableTerminal;

    private boolean withZobristTracker;
    private boolean withStatistics;
    private boolean withDebugSearchTree;

    public TerminalChainBuilder() {
        alphaBetaEvaluation = new AlphaBetaEvaluation();
    }


    public TerminalChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public TerminalChainBuilder withStatistics() {
        this.withStatistics = true;
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


    @Override
    protected  void buildObjects() {
        clearPV = new ClearPV();

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            alphaBetaTerminalNodeStatistics = new AlphaBetaTerminalNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.TERMINAL);
        }

        if (clearPV != null) {
            searchListenerMediator.add(clearPV);
        }
    }

    @Override
    protected  void setupListenerMediator() {
        searchListenerMediator.add(alphaBetaEvaluation);
        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }
        if (alphaBetaTerminalNodeStatistics != null) {
            searchListenerMediator.add(alphaBetaTerminalNodeStatistics);
        }
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
    }

    @Override
    protected void linkObjects() {
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (clearPV != null) {
            chain.add(clearPV);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (alphaBetaTerminalNodeStatistics != null) {
            chain.add(alphaBetaTerminalNodeStatistics);
        }

        chain.add(alphaBetaEvaluation);

        return createChain(chain);
    }
}
