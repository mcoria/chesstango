package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class TerminalChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private GameEvaluator gameEvaluator;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private SmartListenerMediator smartListenerMediator;

    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public TerminalChainBuilder() {
        alphaBetaEvaluation = new AlphaBetaEvaluation();
    }

    public TerminalChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }


    public TerminalChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public TerminalChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public TerminalChainBuilder withDebugSearchTree() {
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
        alphaBetaEvaluation.setGameEvaluator(gameEvaluator);


        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            this.debugFilter = new DebugFilter(DebugNode.NodeTopology.TERMINAL);
            this.debugFilter.setGameEvaluator(gameEvaluator);
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

        chain.add(alphaBetaEvaluation);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugFilter.setNext(next);
            } else if (currentFilter instanceof AlphaBetaEvaluation) {
                //alphaBetaEvaluation.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
