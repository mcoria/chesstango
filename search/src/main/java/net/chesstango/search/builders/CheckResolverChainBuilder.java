package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.debug.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.DebugSorter;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CheckResolverChainBuilder {
    private final AlphaBeta alphaBeta;
    private ExtensionFlowControl extensionFlowControl;
    private GameEvaluator gameEvaluator;
    private DefaultMoveSorter defaultMoveSorter;
    private TranspositionMoveSorterQ transpositionMoveSorterQ;
    private DebugSorter debugSorter;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private TriangularPV triangularPV;
    private SmartListenerMediator smartListenerMediator;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withTranspositionMoveSorter;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public CheckResolverChainBuilder() {
        alphaBeta = new AlphaBeta();
    }

    public CheckResolverChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public CheckResolverChainBuilder withExtensionFlowControl(ExtensionFlowControl extensionFlowControl) {
        this.extensionFlowControl = extensionFlowControl;
        return this;
    }

    public CheckResolverChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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
        this.withTranspositionMoveSorter = true;
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
        this.withDebugSearchTree = true;
        return this;
    }


    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        MoveSorter moveSorter;
        if (withTranspositionMoveSorter) {
            transpositionMoveSorterQ = new TranspositionMoveSorterQ();
            moveSorter = transpositionMoveSorterQ;
        } else {
            defaultMoveSorter = new DefaultMoveSorter();
            moveSorter = defaultMoveSorter;
        }

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
            debugFilter = new DebugFilter(DebugNode.SearchNodeType.CHECK_EXTENSION);
            debugFilter.setGameEvaluator(gameEvaluator);

            if (withTranspositionMoveSorter) {
                debugSorter = new DebugSorter();
                debugSorter.setMoveSorterImp(transpositionMoveSorterQ);
                moveSorter = debugSorter;
            }
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
        alphaBeta.setMoveSorter(moveSorter);
    }

    private void setupListenerMediator() {
        if(defaultMoveSorter != null){
            smartListenerMediator.add(defaultMoveSorter);
        }
        if (transpositionMoveSorterQ != null) {
            smartListenerMediator.add(transpositionMoveSorterQ);
        }
        if (debugSorter != null) {
            smartListenerMediator.add(debugSorter);
        }
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
        if (debugFilter != null) {
            smartListenerMediator.add(debugFilter);
        }
        if (triangularPV != null) {
            smartListenerMediator.add(triangularPV);
        }
        smartListenerMediator.add(alphaBeta);
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

            if (currentFilter instanceof ZobristTracker) {
                zobristQTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableQ) {
                transpositionTableQ.setNext(next);
            } else if (currentFilter instanceof QuiescenceStatisticsExpected) {
                quiescenceStatisticsExpected.setNext(next);
            } else if (currentFilter instanceof AlphaBeta) {
                alphaBeta.setNext(next);
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
