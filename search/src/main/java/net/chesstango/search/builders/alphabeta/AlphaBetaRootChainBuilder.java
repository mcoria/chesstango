package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
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
    private final MoveEvaluationTracker moveEvaluationTracker;
    private final AlphaBeta alphaBeta;
    private final RootMoveSorter rootMoveSorter;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private TranspositionPV transpositionPV;
    private SearchListenerMediator searchListenerMediator;
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

    public AlphaBetaRootChainBuilder() {
        alphaBeta = new AlphaBeta();
        rootMoveSorter = new RootMoveSorter();
        moveEvaluationTracker = new MoveEvaluationTracker();
    }

    public AlphaBetaRootChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
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
            aspirationWindows.setSearchListenerMediator(searchListenerMediator);
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
            moveSorterDebug.setMoveSorterImp(moveSorter);
            moveSorter = moveSorterDebug;
        }

        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }

        if (stopProcessingCatch != null) {
            stopProcessingCatch.setMoveEvaluationTracker(moveEvaluationTracker);
        }
        
        alphaBeta.setMoveSorter(moveSorter);
    }


    private void setupListenerMediator() {
        searchListenerMediator.add(moveEvaluationTracker);

        if (withStatistics) {
            searchListenerMediator.add(alphaBetaStatisticsExpected);
            searchListenerMediator.add(alphaBetaStatisticsVisited);
        }

        if (aspirationWindows != null) {
            searchListenerMediator.add(aspirationWindows);
        }

        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }

        if (moveSorterDebug != null) {
            searchListenerMediator.add(moveSorterDebug);
        }

        if (stopProcessingCatch != null) {
            searchListenerMediator.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }

        if (transpositionTableRoot != null) {
            searchListenerMediator.add(transpositionTableRoot);
        }

        if (transpositionPV != null) {
            searchListenerMediator.add(transpositionPV);
        }

        if (triangularPV != null) {
            searchListenerMediator.add(triangularPV);
        }

        if (alphaBeta != null) {
            searchListenerMediator.add(alphaBeta);
        }

        searchListenerMediator.add(rootMoveSorter);
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

        chain.add(alphaBeta);

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

            switch (currentFilter) {
                case StopProcessingCatch processingCatch -> stopProcessingCatch.setNext(next);
                case ZobristTracker tracker -> zobristTracker.setNext(next);
                case TranspositionTableRoot tableRoot -> transpositionTableRoot.setNext(next);
                case AspirationWindows windows -> aspirationWindows.setNext(next);
                case AlphaBetaStatisticsExpected betaStatisticsExpected -> alphaBetaStatisticsExpected.setNext(next);
                case AlphaBeta beta -> alphaBeta.setNext(next);
                case MoveEvaluationTracker evaluationTracker -> moveEvaluationTracker.setNext(next);
                case AlphaBetaStatisticsVisited betaStatisticsVisited -> alphaBetaStatisticsVisited.setNext(next);
                case DebugFilter filter -> debugFilter.setNext(next);
                case TriangularPV pv -> triangularPV.setNext(next);
                case TranspositionPV pv -> transpositionPV.setNext(next);
                case null, default -> throw new RuntimeException("filter not found");
            }
        }

        return chain.getFirst();
    }
}
