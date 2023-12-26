package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceChainBuilder {
    private final QuiescenceLeafChainBuilder quiescenceLeafChainBuilder;
    private final Quiescence quiescence;
    private final QuiescenceFlowControl quiescenceFlowControl;
    private GameEvaluator gameEvaluator;
    private MoveSorter qMoveSorter;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private TriangularPV triangularPV;
    private DebugTree debugSearchTree;
    private SmartListenerMediator smartListenerMediator;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withTranspositionMoveSorter;
    private boolean withDebugSearchTree;


    public QuiescenceChainBuilder() {
        quiescence = new Quiescence();
        quiescenceLeafChainBuilder = new QuiescenceLeafChainBuilder();
        quiescenceFlowControl = new QuiescenceFlowControl();
    }

    public QuiescenceChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        quiescenceLeafChainBuilder.withGameEvaluator(gameEvaluator);
        return this;
    }

    public QuiescenceChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        quiescenceLeafChainBuilder.withTranspositionTable();
        return this;
    }

    public QuiescenceChainBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        this.withTranspositionMoveSorter = true;
        return this;
    }

    public QuiescenceChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        quiescenceLeafChainBuilder.withZobristTracker();
        return this;
    }

    public QuiescenceChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        quiescenceLeafChainBuilder.withSmartListenerMediator(smartListenerMediator);
        return this;
    }

    public QuiescenceChainBuilder withDebugSearchTree() {
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
        qMoveSorter = withTranspositionMoveSorter ? new TranspositionMoveSorterQ() : new DefaultMoveSorter();

        quiescence.setMoveSorter(qMoveSorter);
        quiescence.setGameEvaluator(gameEvaluator);

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
            this.debugSearchTree = new DebugTree();
            this.debugSearchTree.setGameEvaluator(gameEvaluator);
        }
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(qMoveSorter);
        smartListenerMediator.add(quiescence);
        smartListenerMediator.add(quiescenceFlowControl);
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
        if (debugSearchTree != null) {
            smartListenerMediator.add(debugSearchTree);
        }
    }

    private AlphaBetaFilter createChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

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

        if (debugSearchTree != null) {
            chain.add(debugSearchTree);
        }

        chain.add(quiescenceFlowControl);


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
            } else if (currentFilter instanceof DebugTree) {
                debugSearchTree.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        quiescenceFlowControl.setInteriorNode(chain.get(0));
        quiescenceFlowControl.setLeafNode(quiescenceLeafChainBuilder.build());

        return chain.get(0);
    }
}
