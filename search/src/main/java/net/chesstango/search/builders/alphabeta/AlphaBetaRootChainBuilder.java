package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.core.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.pv.TTPVReaderDebug;
import net.chesstango.search.smart.alphabeta.pv.filters.TranspositionPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.MoveSorterDebug;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.RootMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootChainBuilder extends AbstractChainBuilder {
    private final MoveEvaluationTracker moveEvaluationTracker;
    private final AlphaBeta alphaBeta;
    private final RootMoveSorter rootMoveSorter;
    private final NodeMoveSorter nodeMoveSorter;
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
    private TTPVReader ttPvReader;
    private TTPVReaderDebug ttpvReaderDebug;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public AlphaBetaRootChainBuilder() {
        alphaBeta = new AlphaBeta();
        rootMoveSorter = new RootMoveSorter();
        nodeMoveSorter = new NodeMoveSorter();
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

    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        rootMoveSorter.setNodeMoveSorter(nodeMoveSorter);
        nodeMoveSorter.setMoveComparator(new DefaultMoveComparator());

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

            ttPvReader = new TTPVReader();
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

        if (transpositionPV != null) {
            if (withDebugSearchTree) {
                ttpvReaderDebug = new TTPVReaderDebug();
                ttpvReaderDebug.setImp(ttPvReader);

                transpositionPV.setTtPvReader(ttpvReaderDebug);
            } else {
                transpositionPV.setTtPvReader(ttPvReader);
            }
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

        if (ttPvReader != null) {
            searchListenerMediator.add(ttPvReader);
        }

        if (ttpvReaderDebug != null) {
            searchListenerMediator.add(ttpvReaderDebug);
        }

        searchListenerMediator.add(alphaBeta);
        searchListenerMediator.add(nodeMoveSorter);
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

        return createChain(chain);
    }
}
