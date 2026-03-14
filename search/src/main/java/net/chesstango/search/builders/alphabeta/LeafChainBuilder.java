package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.evaluator.filters.AlphaBetaEvaluation;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaLeafNodeStatistics;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaTerminalNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableLeaf;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

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
    private SearchListenerMediator searchListenerMediator;

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


    /**
     * @return
     */
    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withStatistics) {
            alphaBetaLeafNodeStatistics = new AlphaBetaLeafNodeStatistics();
        }

        if (withDebugSearchTree) {
            debugSearchTree = new DebugFilter(DebugNode.NodeTopology.LEAF);
        }
    }

    private void setupListenerMediator() {
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
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugSearchTree != null) {
            chain.add(debugSearchTree);
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
