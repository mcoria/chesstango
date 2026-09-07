package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluationCache;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaLeafNodeStatistics;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LeafChainBuilder extends AbstractChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private ZobristTracker zobristTracker;
    private AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics;
    private DebugFilter debugSearchTree;
    private ExtendPV extendPV;
    private AlphaBetaEvaluationCache alphaBetaEvaluationCache;

    private boolean withZobristTracker;
    private boolean withStatistics;
    private boolean withDebugSearchTree;
    private boolean withGameEvaluatorCache;


    public LeafChainBuilder() {
        leaf = new AlphaBetaEvaluation();
    }

    public LeafChainBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
        return this;
    }

    public LeafChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public LeafChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public LeafChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public LeafChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        return this;
    }


    @Override
    protected void buildObjects() {
        extendPV = new ExtendPV();

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            alphaBetaLeafNodeStatistics = new AlphaBetaLeafNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(NodeTopology.LEAF);
        }

        if (withGameEvaluatorCache) {
            alphaBetaEvaluationCache = new AlphaBetaEvaluationCache();
        }
    }

    @Override
    protected void setupListenerMediator() {
        listenerMediator.add(leaf);

        if (zobristTracker != null) {
            listenerMediator.add(zobristTracker);
        }

        if (alphaBetaLeafNodeStatistics != null) {
            listenerMediator.add(alphaBetaLeafNodeStatistics);
        }

        if (debugSearchTree != null) {
            listenerMediator.add(debugSearchTree);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }

        if (alphaBetaEvaluationCache != null) {
            listenerMediator.add(alphaBetaEvaluationCache);
        }
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugSearchTree != null) {
            chain.add(debugSearchTree);
        }

        if (extendPV != null) {
            chain.add(extendPV);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (alphaBetaLeafNodeStatistics != null) {
            chain.add(alphaBetaLeafNodeStatistics);
        }

        if (alphaBetaEvaluationCache != null) {
            chain.add(alphaBetaEvaluationCache);
        }

        chain.add(leaf);

        return createChain(chain);
    }
}
