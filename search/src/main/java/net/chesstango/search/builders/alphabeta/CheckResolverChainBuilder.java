package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.QuiescenceStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CheckResolverChainBuilder {
    private final AlphaBeta alphaBeta;
    private final MoveSorterBuilder moveSorterBuilder;
    private ExtensionFlowControl extensionFlowControl;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
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

    public CheckResolverChainBuilder withExtensionFlowControl(ExtensionFlowControl extensionFlowControl) {
        this.extensionFlowControl = extensionFlowControl;
        return this;
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
            quiescenceStatisticsExpected = new QuiescenceStatisticsExpected();
            quiescenceStatisticsVisited = new QuiescenceStatisticsVisited();
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
            searchListenerMediator.addAcceptor(quiescenceStatisticsExpected);
            searchListenerMediator.addAcceptor(quiescenceStatisticsVisited);
        }
        if (zobristQTracker != null) {
            searchListenerMediator.addAcceptor(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            searchListenerMediator.addAcceptor(transpositionTableQ);
        }
        if (debugFilter != null) {
            searchListenerMediator.addAcceptor(debugFilter);
        }
        if (triangularPV != null) {
            searchListenerMediator.addAcceptor(triangularPV);
        }
        searchListenerMediator.addAcceptor(alphaBeta);
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

        if (quiescenceStatisticsExpected != null) {
            chain.add(quiescenceStatisticsExpected);
        }

        chain.add(alphaBeta);

        if (quiescenceStatisticsVisited != null) {
            chain.add(quiescenceStatisticsVisited);
        }

        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        chain.add(extensionFlowControl);


        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case ZobristTracker zobristTracker -> zobristQTracker.setNext(next);
                case TranspositionTableQ tableQ -> transpositionTableQ.setNext(next);
                case QuiescenceStatisticsExpected statisticsExpected -> quiescenceStatisticsExpected.setNext(next);
                case AlphaBeta beta -> alphaBeta.setNext(next);
                case QuiescenceStatisticsVisited statisticsVisited -> quiescenceStatisticsVisited.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case TriangularPV pv -> triangularPV.setNext(next);
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
