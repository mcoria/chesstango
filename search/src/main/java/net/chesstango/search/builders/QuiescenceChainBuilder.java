package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.TrapEvaluation;
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
    private GameEvaluator gameEvaluator;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private TrapEvaluation trapEvaluation;
    private TriangularPV triangularPV;

    private SmartListenerMediator smartListenerMediator;

    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public QuiescenceChainBuilder() {
        quiescence = new Quiescence();
        moveSorterBuilder = new MoveSorterQuiescenceBuilder();
    }

    public QuiescenceChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public QuiescenceChainBuilder withExtensionFlowControl(ExtensionFlowControl extensionFlowControl) {
        this.extensionFlowControl = extensionFlowControl;
        return this;
    }

    public QuiescenceChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.moveSorterBuilder.withSmartListenerMediator(smartListenerMediator);
        this.smartListenerMediator = smartListenerMediator;
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

    public QuiescenceChainBuilder withGameEvaluatorCache(GameEvaluatorCache gameEvaluatorCache) {
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
            quiescence.setGameEvaluator(trapEvaluation);
        } else {
            quiescence.setGameEvaluator(gameEvaluator);
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
            trapEvaluation = new TrapEvaluation();
            trapEvaluation.setGameEvaluator(gameEvaluator);
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
    }

    private void setupListenerMediator() {
        if (withStatistics) {
            smartListenerMediator.add(quiescenceStatisticsExpected);
            smartListenerMediator.add(quiescenceStatisticsVisited);
        }
        if (zobristQTracker != null) {
            smartListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            smartListenerMediator.add(transpositionTableQ);
        }
        if (withDebugSearchTree) {
            smartListenerMediator.add(debugFilter);
            smartListenerMediator.add(trapEvaluation);
        }
        if (triangularPV != null) {
            smartListenerMediator.add(triangularPV);
        }
        smartListenerMediator.add(quiescence);
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

            if (currentFilter instanceof ZobristTracker) {
                zobristQTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableQ) {
                transpositionTableQ.setNext(next);
            } else if (currentFilter instanceof QuiescenceStatisticsExpected) {
                quiescenceStatisticsExpected.setNext(next);
            } else if (currentFilter instanceof Quiescence) {
                quiescence.setNext(next);
            } else if (currentFilter instanceof QuiescenceStatisticsVisited) {
                quiescenceStatisticsVisited.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugFilter.setNext(next);
            } else if (currentFilter instanceof TriangularPV) {
                triangularPV.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.get(0);
    }
}
