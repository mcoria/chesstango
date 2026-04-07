package net.chesstango.search.builders.alphabeta;


import lombok.Getter;
import net.chesstango.search.builders.sorters.MoveSorterRootBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.TTPVReader;
import net.chesstango.search.smart.alphabeta.pv.TTPVReaderDebug;
import net.chesstango.search.smart.alphabeta.pv.filters.TranspositionPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.alphabeta.root.filters.AspirationWindows;
import net.chesstango.search.smart.alphabeta.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaRootNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootChainBuilder extends AbstractChainBuilder {
    private final RootMoveEvaluationTracker moveEvaluationTracker;

    @Getter
    private final RootMoveEvaluationCollection moveEvaluations;

    private final AlphaBeta alphaBeta;

    private final MoveSorterRootBuilder moveSorterRootBuilder;

    private AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private TranspositionPV transpositionPV;
    private SearchListenerMediator searchListenerMediator;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;
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
        moveSorterRootBuilder = new MoveSorterRootBuilder();
        moveEvaluationTracker = new RootMoveEvaluationTracker();
        moveEvaluations = new RootMoveEvaluationCollection();
    }

    public AlphaBetaRootChainBuilder withIterativeDeepening() {
        moveSorterRootBuilder.withIterativeDeepening();
        return this;
    }

    public AlphaBetaRootChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaRootChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.searchListenerMediator = searchListenerMediator;
        this.moveSorterRootBuilder.withSmartListenerMediator(searchListenerMediator);
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
        moveSorterRootBuilder.withDebugSearchTree();
        return this;
    }

    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        alphaBeta.setMoveSorter(moveSorterRootBuilder.build());

        return createChain();
    }

    private void buildObjects() {
        moveEvaluationTracker.setMoveEvaluations(moveEvaluations);

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
            aspirationWindows.setSearchListenerMediator(searchListenerMediator);
        }

        if (withStatistics) {
            alphaBetaRootNodeStatistics = new AlphaBetaRootNodeStatistics();
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
        }

        if (transpositionPV != null) {
            if (withDebugSearchTree) {
                ttpvReaderDebug = new TTPVReaderDebug();
                ttpvReaderDebug.setImp(ttPvReader);

                transpositionPV.setPvReader(ttpvReaderDebug);
            } else {
                transpositionPV.setPvReader(ttPvReader);
            }
        }


        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }

        if (stopProcessingCatch != null) {
            stopProcessingCatch.setRootMoveEvaluationCollection(moveEvaluations);
        }
    }


    private void setupListenerMediator() {
        searchListenerMediator.add(moveEvaluationTracker);
        searchListenerMediator.add(moveEvaluations);

        if (stopProcessingCatch != null) {
            searchListenerMediator.add(stopProcessingCatch);
        }

        if (zobristTracker != null) {
            searchListenerMediator.add(zobristTracker);
        }

        if (aspirationWindows != null) {
            searchListenerMediator.add(aspirationWindows);
        }

        if (debugFilter != null) {
            searchListenerMediator.add(debugFilter);
        }

        if (alphaBetaRootNodeStatistics != null) {
            searchListenerMediator.add(alphaBetaRootNodeStatistics);
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

        if (alphaBetaRootNodeStatistics != null) {
            chain.add(alphaBetaRootNodeStatistics);
        }

        if (transpositionTableRoot != null) {
            chain.add(transpositionTableRoot);
        }

        chain.add(alphaBeta);

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
