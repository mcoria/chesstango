package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceLeafChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private GameEvaluator gameEvaluator;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugSearchTree;
    private SmartListenerMediator smartListenerMediator;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;


    public QuiescenceLeafChainBuilder() {
        leaf = new AlphaBetaEvaluation();
    }

    public QuiescenceLeafChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }


    public QuiescenceLeafChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceLeafChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public QuiescenceLeafChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public QuiescenceLeafChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }


    /**
     * <p>
     * <p>
     * *  QuiescenceStatics -> ZobristTracker -> TranspositionTableQ -> QuiescenceFlowControl -> Quiescence
     * *            ^                                                                              |
     * *            |                                                                              |
     * *            -------------------------------------------------------------------------------
     *
     * @return
     */
    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        leaf.setGameEvaluator(gameEvaluator);

        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }

        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }

        if (withDebugSearchTree) {
            this.debugSearchTree = new DebugFilter(DebugNode.SearchNodeType.Q_LEAF);
            this.debugSearchTree.setGameEvaluator(gameEvaluator);
        }
    }

    private void setupListenerMediator() {
        if (zobristQTracker != null) {
            smartListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            smartListenerMediator.add(transpositionTableQ);
        }
        if (debugSearchTree != null) {
            smartListenerMediator.add(debugSearchTree);
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

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        chain.add(leaf);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristQTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableQ) {
                transpositionTableQ.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugSearchTree.setNext(next);
            } else if (currentFilter instanceof AlphaBetaEvaluation) {
                //leaf
            } else {
                throw new RuntimeException("filter not found");
            }
        }


        return chain.get(0);
    }
}
