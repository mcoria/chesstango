package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.debug.DebugTree;
import net.chesstango.search.smart.debug.SearchNode;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaTerminalChainBuilder {
    private final AlphaBetaEvaluation alphaBetaEvaluation;
    private GameEvaluator gameEvaluator;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;
    private DebugTree debugTree;
    private SmartListenerMediator smartListenerMediator;

    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public AlphaBetaTerminalChainBuilder() {
        alphaBetaEvaluation = new AlphaBetaEvaluation();
    }

    public AlphaBetaTerminalChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public AlphaBetaTerminalChainBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }


    public AlphaBetaTerminalChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaTerminalChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaTerminalChainBuilder withDebugSearchTree() {
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
            this.debugTree = new DebugTree(SearchNode.SearchNodeType.TERMINAL);
            this.debugTree.setGameEvaluator(gameEvaluator);
        }

    }

    private void setupListenerMediator() {
        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }
        if (transpositionTable != null) {
            smartListenerMediator.add(transpositionTable);
        }
        if (debugTree != null) {
            smartListenerMediator.add(debugTree);
        }
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugTree != null) {
            chain.add(debugTree);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (transpositionTable != null) {
            chain.add(transpositionTable);
        }

        chain.add(alphaBetaEvaluation);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTable) {
                transpositionTable.setNext(next);
            } else if (currentFilter instanceof DebugTree) {
                debugTree.setNext(next);
            } else if (currentFilter instanceof AlphaBeta) {
                //alphaBetaTerminal.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
