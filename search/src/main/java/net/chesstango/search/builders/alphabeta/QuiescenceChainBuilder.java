package net.chesstango.search.builders.alphabeta;


import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.builders.MoveSorterQuiescenceBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.evaluator.EvaluatorDebug;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.AlphaBetaQuiescenceNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class QuiescenceChainBuilder extends AbstractChainBuilder {
    private final Quiescence quiescence;
    private final MoveSorterQuiescenceBuilder moveSorterBuilder;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private AlphaBetaQuiescenceNodeStatistics alphaBetaQuiescenceNodeStatistics;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private EvaluatorDebug gameEvaluatorDebug;
    private TriangularPV triangularPV;

    private SearchListenerMediator searchListenerMediator;

    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withTriangularPV;


    public QuiescenceChainBuilder() {
        quiescence = new Quiescence();
        moveSorterBuilder = new MoveSorterQuiescenceBuilder();
    }

    public QuiescenceChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }
    public QuiescenceChainBuilder withSmartListenerMediator(SearchListenerMediator searchListenerMediator) {
        this.moveSorterBuilder.withSmartListenerMediator(searchListenerMediator);
        this.searchListenerMediator = searchListenerMediator;
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

    public QuiescenceChainBuilder withGameEvaluatorCache(EvaluatorCache gameEvaluatorCache) {
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
            quiescence.setEvaluator(gameEvaluatorDebug);
        }

        return createChain();
    }

    private void buildObjects() {
        if (withStatistics) {
            alphaBetaQuiescenceNodeStatistics = new AlphaBetaQuiescenceNodeStatistics();
        }
        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }
        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }
        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(DebugNode.NodeTopology.QUIESCENCE);
            gameEvaluatorDebug = new EvaluatorDebug();
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
    }

    private void setupListenerMediator() {
        if (withStatistics) {
            searchListenerMediator.add(alphaBetaQuiescenceNodeStatistics);
        }
        if (zobristQTracker != null) {
            searchListenerMediator.add(zobristQTracker);
        }
        if (transpositionTableQ != null) {
            searchListenerMediator.add(transpositionTableQ);
        }
        if (withDebugSearchTree) {
            searchListenerMediator.add(debugFilter);
            searchListenerMediator.add(gameEvaluatorDebug);
        }
        if (triangularPV != null) {
            searchListenerMediator.add(triangularPV);
        }
        searchListenerMediator.add(quiescence);
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

        if (alphaBetaQuiescenceNodeStatistics != null) {
            chain.add(alphaBetaQuiescenceNodeStatistics);
        }

        chain.add(quiescence);

        if (triangularPV != null) {
            chain.add(triangularPV);
        }

        chain.add(alphaBetaFlowControl);


        return createChain(chain);
    }
}
