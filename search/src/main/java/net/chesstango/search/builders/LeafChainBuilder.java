package net.chesstango.search.builders;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LeafChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private GameEvaluator gameEvaluator;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugSearchTree;
    private SmartListenerMediator smartListenerMediator;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;

    public LeafChainBuilder() {
        leaf = new AlphaBetaEvaluation();
    }

    public LeafChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }


    public LeafChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public LeafChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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
        leaf.setGameEvaluator(gameEvaluator);

        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(DebugNode.NodeTopology.LEAF);
        }
    }

    private void setupListenerMediator() {
        if (zobristQTracker != null) {
            smartListenerMediator.add(zobristQTracker);
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

        chain.add(leaf);

        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof ZobristTracker) {
                zobristQTracker.setNext(next);
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
