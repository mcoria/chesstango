package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.evaluator.EvaluatorDebug;
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
public class QuiescenceChainBuilder {
    private final Quiescence quiescence;
    private final MoveSorterQuiescenceBuilder moveSorterBuilder;
    private ExtensionFlowControl extensionFlowControl;
    private Evaluator evaluator;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private EvaluatorDebug gameEvaluatorDebug;
    private TriangularPV triangularPV;

    private SearchListenerMediator searchListenerMediator;

    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public QuiescenceChainBuilder() {
        quiescence = new Quiescence();
        moveSorterBuilder = new MoveSorterQuiescenceBuilder();
    }

    public QuiescenceChainBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public QuiescenceChainBuilder withExtensionFlowControl(ExtensionFlowControl extensionFlowControl) {
        this.extensionFlowControl = extensionFlowControl;
        return this;
    }

    public QuiescenceChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        this.searchListenerMediator = searchListenerMediator;
        return this;
    }

    public QuiescenceChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        moveSorterBuilder.withTranspositionTable();
        return this;
    }

    public QuiescenceChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public QuiescenceChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public QuiescenceChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public QuiescenceChainBuilder withGameEvaluatorCache(EvaluatorCache gameEvaluatorCache) {
        moveSorterBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        return this;
    }

    public QuiescenceChainBuilder withRecaptureSorter() {
        moveSorterBuilder.withRecaptureSorter();
        return this;
    }

    public QuiescenceChainBuilder withMvvLvaSorter() {
        moveSorterBuilder.withMvvLva();
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

        quiescence.setMoveSorter(moveSorterBuilder.build());

        if (withDebugSearchTree) {
            quiescence.setEvaluator(gameEvaluatorDebug);
        } else {
            quiescence.setEvaluator(evaluator);
        }

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
            debugFilter = new DebugFilter(DebugNode.NodeTopology.QUIESCENCE);
            gameEvaluatorDebug = new EvaluatorDebug();
            gameEvaluatorDebug.setEvaluator(evaluator);
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
        if (withDebugSearchTree) {
            searchListenerMediator.addAcceptor(debugFilter);
            searchListenerMediator.addAcceptor(gameEvaluatorDebug);
        }
        if (triangularPV != null) {
            searchListenerMediator.addAcceptor(triangularPV);
        }
        searchListenerMediator.addAcceptor(quiescence);
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

        chain.add(quiescence);

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
                case ZobristTracker zobristTracker -> zobristTracker.setNext(next);
                case TranspositionTableQ tableQ -> tableQ.setNext(next);
                case QuiescenceStatisticsExpected statisticsExpected -> statisticsExpected.setNext(next);
                case Quiescence quiescence1 -> quiescence1.setNext(next);
                case QuiescenceStatisticsVisited statisticsVisited -> statisticsVisited.setNext(next);
                case DebugFilter filter -> filter.setNext(next);
                case TriangularPV pv -> pv.setNext(next);
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
