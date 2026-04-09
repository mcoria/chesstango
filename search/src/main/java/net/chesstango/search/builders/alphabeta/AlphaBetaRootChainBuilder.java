package net.chesstango.search.builders.alphabeta;


import lombok.Getter;
import net.chesstango.search.builders.sorters.MoveSorterRootBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorDebug;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTransposition;
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.pv.filters.TranspositionPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularTriggerPV;
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
    private TriangularTriggerPV triangularTriggerPV;
    private TriangularPV triangularPV;
    private PVCalculatorTriangular trianglePVReader;
    private AlphaBetaFilter alphaBetaFlowControl;
    private PVCalculatorTransposition transpositionPVReader;
    private PVCalculatorDebug ttpvReaderDebug;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;


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
            transpositionPVReader = new PVCalculatorTransposition();

            if (withDebugSearchTree) {
                ttpvReaderDebug = new PVCalculatorDebug();
                ttpvReaderDebug.setImp(transpositionPVReader);

                transpositionPV.setPvCalculator(ttpvReaderDebug);
            } else {
                transpositionPV.setPvCalculator(transpositionPVReader);
            }
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.ROOT);
        }

        if (!withTranspositionTable) {
            triangularTriggerPV = new TriangularTriggerPV();
            triangularPV = new TriangularPV();
            trianglePVReader = new PVCalculatorTriangular();

            if (withDebugSearchTree) {
                ttpvReaderDebug = new PVCalculatorDebug();
                ttpvReaderDebug.setImp(trianglePVReader);

                triangularTriggerPV.setPvCalculator(ttpvReaderDebug);
            } else {
                triangularTriggerPV.setPvCalculator(trianglePVReader);
            }
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

        if (trianglePVReader != null) {
            searchListenerMediator.add(trianglePVReader);
        }

        if (transpositionPVReader != null) {
            searchListenerMediator.add(transpositionPVReader);
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

        if (triangularTriggerPV != null) {
            chain.add(triangularTriggerPV);
        }

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
