package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.builders.sorters.MoveSorterRootBuilder;
import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.debug.model.NodeTopology;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.pv.model.PVCalculatorTriangular;
import net.chesstango.search.smart.root.RootMoveEvaluationBest;
import net.chesstango.search.smart.root.RootMoveEvaluationCollection;
import net.chesstango.search.smart.root.filters.AspirationWindows;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.root.visitors.LinkRootMoveEvaluationObjectsVisitor;
import net.chesstango.search.smart.statistics.node.filters.AlphaBetaRootNodeStatistics;
import net.chesstango.search.smart.transposition.filters.TranspositionTableRoot;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;
import net.chesstango.search.sorters.MoveSorter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaRootChainBuilder extends AbstractChainBuilder {
    private final RootMoveEvaluationTracker rootMoveEvaluationTracker;
    private final RootMoveEvaluationBest rootMoveEvaluationBest;
    private final RootMoveEvaluationCollection rootMoveEvaluationCollection;

    private final ExtendPV extendPV;
    private final PropagatePV propagatePV;
    private final PVCalculatorTriangular pvCalculatorTriangular;

    private final AlphaBeta alphaBeta;

    private final MoveSorterRootBuilder moveSorterRootBuilder;

    private AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics;
    private StopProcessingCatch stopProcessingCatch;
    private AspirationWindows aspirationWindows;
    private TranspositionTableRoot transpositionTableRoot;
    private ZobristTracker zobristTracker;
    private DebugFilter debugFilter;

    private AlphaBetaFilter alphaBetaFlowControl;

    private MoveSorter moveSorter;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withDebugSearchTree;


    public AlphaBetaRootChainBuilder() {
        alphaBeta = new AlphaBeta();
        moveSorterRootBuilder = new MoveSorterRootBuilder();

        rootMoveEvaluationTracker = new RootMoveEvaluationTracker();
        rootMoveEvaluationBest = new RootMoveEvaluationBest();
        rootMoveEvaluationCollection = new RootMoveEvaluationCollection();

        extendPV = new ExtendPV();
        propagatePV = new PropagatePV();
        pvCalculatorTriangular = new PVCalculatorTriangular();
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
        rootMoveEvaluationTracker.setRootMoveEvaluationBest(rootMoveEvaluationBest);
        rootMoveEvaluationTracker.setRootMoveEvaluationCollection(rootMoveEvaluationCollection);

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
            debugFilter = new DebugFilter(NodeTopology.ROOT);
        }

        moveSorter = moveSorterRootBuilder.build();
    }


    @Override
    protected void setupListenerMediator() {
        searchListenerMediator.add(rootMoveEvaluationTracker);

        searchListenerMediator.add(rootMoveEvaluationBest);

        searchListenerMediator.add(rootMoveEvaluationCollection);

        searchListenerMediator.add(extendPV);

        searchListenerMediator.add(propagatePV);

        searchListenerMediator.add(pvCalculatorTriangular);

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
    }

    @Override
    public void link() {
        alphaBeta.setMoveSorter(moveSorter);

        rootMoveEvaluationTracker.setPvCalculator(pvCalculatorTriangular);

        if (withAspirationWindows) {
            aspirationWindows.setSearchListenerMediator(searchListenerMediator);
        }

        searchListenerMediator.accept(new LinkRootMoveEvaluationObjectsVisitor(rootMoveEvaluationBest, new LinkedList<>()));
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

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        chain.add(extendPV);

        if (alphaBetaRootNodeStatistics != null) {
            chain.add(alphaBetaRootNodeStatistics);
        }

        if (transpositionTableRoot != null) {
            chain.add(transpositionTableRoot);
        }

        chain.add(alphaBeta);

        chain.add(rootMoveEvaluationTracker);

        chain.add(propagatePV);

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }
}
