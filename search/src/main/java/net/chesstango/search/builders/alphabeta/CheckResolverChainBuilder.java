package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.MoveSorterBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaInteriorNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CheckResolverChainBuilder extends AbstractChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterBuilder moveSorterBuilder;
    private AlphaBetaInteriorNodeStatistics alphaBetaNodeStatistics;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private TriangularPV triangularPV;
    private SearchListenerMediator searchListenerMediator;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public CheckResolverChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterBuilder = new MoveSorterBuilder();
    }

    public CheckResolverChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        return this;
    }

    public CheckResolverChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public CheckResolverChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public CheckResolverChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        moveSorterBuilder.withTranspositionTable();
        return this;
    }

    public CheckResolverChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public CheckResolverChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public CheckResolverChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }


    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        alphaBeta.setMoveSorter(moveSorterBuilder.build());

        return createChain();
    }

    private void buildObjects() {
        if (withStatistics) {
            alphaBetaNodeStatistics = new AlphaBetaInteriorNodeStatistics();
        }
        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }
        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }
        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.CHECK_EXTENSION);
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
    }

    private void setupListenerMediator() {
        if (withStatistics) {
            searchListenerMediator.add(alphaBetaNodeStatistics);
        }
        if (zobristQTracker != null) {
            searchListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            searchListenerMediator.add(transpositionTableQ);
        }
        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }
        if (triangularPV != null) {
            searchListenerMediator.add(triangularPV);
        }
        searchListenerMediator.add(alphaBeta);
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        if (alphaBetaNodeStatistics != null) {
            chain.add(alphaBetaNodeStatistics);
        }

        chain.add(alphaBeta);

        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        //chain.add(extensionFlowControl);


        return createChain(chain);
    }
}
