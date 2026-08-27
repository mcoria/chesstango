package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaTerminalNodeStatistics;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;

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
    private ExtendPV extendPV;


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

    public TerminalChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }

    public TerminalChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }


    @Override
    protected  void buildObjects() {
        extendPV = new ExtendPV();

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            alphaBetaTerminalNodeStatistics = new AlphaBetaTerminalNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.TERMINAL);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }
    }

    @Override
    protected  void setupListenerMediator() {
        listenerMediator.add(alphaBetaEvaluation);
        if (zobristTracker != null) {
            listenerMediator.add(zobristTracker);
        }
        if (alphaBetaTerminalNodeStatistics != null) {
            listenerMediator.add(alphaBetaTerminalNodeStatistics);
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

        if (alphaBetaTerminalNodeStatistics != null) {
            chain.add(alphaBetaTerminalNodeStatistics);
        }

        chain.add(alphaBetaEvaluation);

        return createChain(chain);
    }
}
