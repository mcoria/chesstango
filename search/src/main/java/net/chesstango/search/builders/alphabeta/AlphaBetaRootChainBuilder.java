package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaRootExplorer;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.features.debug.filters.DebugFilter;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.pv.filters.TranspositionPV;
import net.chesstango.search.smart.features.pv.filters.TriangularPV;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.features.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.features.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.features.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.RootMoveSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootChainBuilder {
    private final RootMoveSorter rootMoveSorter;
    private final MoveEvaluationTracker moveEvaluationTracker;
    private AlphaBeta alphaBeta;
    private AlphaBetaRootExplorer alphaBetaRootExplorer;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private TranspositionPV transpositionPV;
    private SmartListenerMediator smartListenerMediator;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
    private MoveSorterDebug moveSorterDebug;
    private TriangularPV triangularPV;
    private AlphaBetaFilter alphaBetaFlowControl;
    private Evaluator evaluator;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;
    private boolean withExploreMove;

    public AlphaBetaRootChainBuilder() {
        rootMoveSorter = new RootMoveSorter();
        moveEvaluationTracker = new MoveEvaluationTracker();
    }

    public AlphaBetaRootChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaRootChainBuilder withStopProcessingCatch() {
        stopProcessingCatch = new StopProcessingCatch();
        return this;
    }


    public AlphaBetaRootChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public AlphaBetaRootChainBuilder withAspirationWindows() {
        this.withAspirationWindows = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withDebugSearchTree() {
        this.withDebugSearchTree = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public AlphaBetaRootChainBuilder withExploreMove() {
        this.withExploreMove = true;
        return this;
    }

    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        MoveSorter moveSorter = rootMoveSorter;

        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
            aspirationWindows.setSmartListenerMediator(smartListenerMediator);
        }

        if (withTranspositionTable) {
            transpositionTableRoot = new TranspositionTableRoot();

            transpositionPV = new TranspositionPV();
            transpositionPV.setEvaluator(evaluator);
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }


        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.ROOT);

            moveSorterDebug = new MoveSorterDebug();
            moveSorterDebug.setMoveSorterImp(rootMoveSorter);

            moveSorter = moveSorterDebug;
        }

        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }

        if (stopProcessingCatch != null) {
            stopProcessingCatch.setMoveEvaluationTracker(moveEvaluationTracker);
        }

        if (!withExploreMove) {
            alphaBeta = new AlphaBeta();
            alphaBeta.setMoveSorter(moveSorter);
        } else {
            alphaBetaRootExplorer = new AlphaBetaRootExplorer();
            alphaBetaRootExplorer.setMoveSorter(moveSorter);
        }

    }


    private void setupListenerMediator() {
        smartListenerMediator.add(moveEvaluationTracker);

        if (withStatistics) {
            smartListenerMediator.add(alphaBetaStatisticsExpected);
            smartListenerMediator.add(alphaBetaStatisticsVisited);
        }

        if (aspirationWindows != null) {
            smartListenerMediator.add(aspirationWindows);
        }

        if (debugFilter != null) {
            smartListenerMediator.add(debugFilter);
        }

        if (moveSorterDebug != null) {
            smartListenerMediator.add(moveSorterDebug);
        }

        if (stopProcessingCatch != null) {
            smartListenerMediator.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }

        if (transpositionTableRoot != null) {
            smartListenerMediator.add(transpositionTableRoot);
        }

        if (transpositionPV != null) {
            smartListenerMediator.add(transpositionPV);
        }

        if (triangularPV != null) {
            smartListenerMediator.add(triangularPV);
        }

        if (alphaBeta != null) {
            smartListenerMediator.add(alphaBeta);
        } else if (alphaBetaRootExplorer != null) {
            smartListenerMediator.add(alphaBetaRootExplorer);
        }
        smartListenerMediator.add(rootMoveSorter);
    }


    private AlphaBetaFilter createChain() {

        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (stopProcessingCatch != null) {
            chain.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            chain.add(zobristTracker);
        }

        if (aspirationWindows != null) {
            chain.add(aspirationWindows);
        }

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (transpositionTableRoot != null) {
            chain.add(transpositionTableRoot);
        }

        if (alphaBetaStatisticsExpected != null) {
            chain.add(alphaBetaStatisticsExpected);
        }

        if (alphaBeta != null) {
            chain.add(alphaBeta);
        } else if (alphaBetaRootExplorer != null) {
            chain.add(alphaBetaRootExplorer);
        }

        if (alphaBetaStatisticsVisited != null) {
            chain.add(alphaBetaStatisticsVisited);
        }

        chain.add(moveEvaluationTracker);

        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        if (transpositionPV != null) {
            chain.add(transpositionPV);
        }

        chain.add(alphaBetaFlowControl);


        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            if (currentFilter instanceof StopProcessingCatch) {
                stopProcessingCatch.setNext(next);
            } else if (currentFilter instanceof ZobristTracker) {
                zobristTracker.setNext(next);
            } else if (currentFilter instanceof TranspositionTableRoot) {
                transpositionTableRoot.setNext(next);
            } else if (currentFilter instanceof AspirationWindows) {
                aspirationWindows.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsExpected) {
                alphaBetaStatisticsExpected.setNext(next);
            } else if (currentFilter instanceof AlphaBeta) {
                alphaBeta.setNext(next);
            } else if (currentFilter instanceof AlphaBetaRootExplorer) {
                alphaBetaRootExplorer.setNext(next);
            }else if (currentFilter instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(next);
            } else if (currentFilter instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(next);
            } else if (currentFilter instanceof DebugFilter) {
                debugFilter.setNext(next);
            } else if (currentFilter instanceof TriangularPV) {
                triangularPV.setNext(next);
            } else if (currentFilter instanceof TranspositionPV) {
                transpositionPV.setNext(next);
            } else {
                throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
