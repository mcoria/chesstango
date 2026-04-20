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
import net.chesstango.search.smart.alphabeta.pv.PVCalculatorTriangular;
import net.chesstango.search.smart.alphabeta.pv.filters.CalculatePV;
import net.chesstango.search.smart.alphabeta.pv.filters.UpdatePV;
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
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;

    private UpdatePV updatePV;
    private CalculatePV calculatePV;
    private PVCalculatorTriangular pvCalculatorTriangular;
    private PVCalculatorDebug pvCalculatorDebug;

    private AlphaBetaFilter alphaBetaFlowControl;


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


    @Override
    protected void buildObjects() {
        moveEvaluationTracker.setRootMoveEvaluationCollection(moveEvaluations);

        calculatePV = new CalculatePV();

        pvCalculatorTriangular = new PVCalculatorTriangular();

        updatePV = new UpdatePV();

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
        }

        if (withStatistics) {
            alphaBetaRootNodeStatistics = new AlphaBetaRootNodeStatistics();
        }

        if (withTranspositionTable) {
            transpositionTableRoot = new TranspositionTableRoot();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.ROOT);
        }

        if (withDebugSearchTree) {
            pvCalculatorDebug = new PVCalculatorDebug();
        }

        if (stopProcessingCatch != null) {
            stopProcessingCatch.setRootMoveEvaluationCollection(moveEvaluations);
        }
    }


    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(moveEvaluationTracker);

        searchListenerMediator.add(moveEvaluations);

        searchListenerMediator.add(alphaBeta);

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

        if (calculatePV != null) {
            searchListenerMediator.add(calculatePV);
        }

        if (updatePV != null) {
            searchListenerMediator.add(updatePV);
        }

        if (pvCalculatorTriangular != null) {
            searchListenerMediator.add(pvCalculatorTriangular);
        }

        if (pvCalculatorDebug != null) {
            searchListenerMediator.add(pvCalculatorDebug);
        }
    }

    @Override
    protected void linkObjects() {
        alphaBeta.setMoveSorter(moveSorterRootBuilder.build());

        if (withAspirationWindows) {
            aspirationWindows.setSearchListenerMediator(searchListenerMediator);
        }

        if (withDebugSearchTree) {
            pvCalculatorDebug.setImp(pvCalculatorTriangular);

            calculatePV.setPvCalculator(pvCalculatorDebug);
        } else {
            calculatePV.setPvCalculator(pvCalculatorTriangular);
        }
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
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

        if (calculatePV != null) {
            chain.add(calculatePV);
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

        if (updatePV != null) {
            chain.add(updatePV);
        }

        chain.add(moveEvaluationTracker);

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
