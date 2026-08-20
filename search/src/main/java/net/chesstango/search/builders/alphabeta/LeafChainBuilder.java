package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.evaluator.filters.AlphaBetaEvaluation;
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

    /**
     * TranspositionTableLeaf escribe demasiadas entradas en TT y sobreescribe aquellas entradas que si interesan
     */
    //private TranspositionTableLeaf transpositionTable;

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

    public LeafChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }


    @Override
    protected  void buildObjects() {
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
    }

    @Override
    protected  void setupListenerMediator() {
        searchListenerMediator.add(leaf);

        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }

        if (alphaBetaLeafNodeStatistics != null) {
            searchListenerMediator.add(alphaBetaLeafNodeStatistics);
        }

        if (debugSearchTree != null) {
            searchListenerMediator.add(debugSearchTree);
        }

        if (extendPV != null) {
            searchListenerMediator.add(extendPV);
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

        chain.add(leaf);

        return createChain(chain);
    }
}
