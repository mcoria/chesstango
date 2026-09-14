package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.alphabeta.statistics.node.filters.LeafNodeStatistics;
import net.chesstango.search.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class LeafChainBuilder extends AbstractChainBuilder {
    private final AlphaBetaEvaluation leaf;
    private ZobristTracker zobristTracker;
    private LeafNodeStatistics leafNodeStatistics;
    private DebugFilter debugSearchTree;
    private ExtendPV extendPV;

    private boolean withZobristTracker;
    private boolean withStatistics;
    private boolean withDebugSearchTree;

    public LeafChainBuilder() {
        leaf = new AlphaBetaEvaluation();
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
            leafNodeStatistics = new LeafNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(NodeTopology.LEAF);
        }
    }

    @Override
    protected void setupListenerMediator() {
        listenerMediator.add(leaf);

        if (zobristTracker != null) {
            listenerMediator.add(zobristTracker);
        }

        if (leafNodeStatistics != null) {
            listenerMediator.add(leafNodeStatistics);
        }

        if (debugSearchTree != null) {
            listenerMediator.add(debugSearchTree);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
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

        if (leafNodeStatistics != null) {
            chain.add(leafNodeStatistics);
        }

        chain.add(leaf);

        return createChain(chain);
    }
}
