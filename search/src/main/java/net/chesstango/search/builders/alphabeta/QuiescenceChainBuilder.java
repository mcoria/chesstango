package net.chesstango.search.builders.alphabeta;


import net.chesstango.search.ListenerMediator;
import net.chesstango.search.alphabeta.AlphaBetaFilter;
import net.chesstango.search.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.alphabeta.debug.model.NodeTopology;
import net.chesstango.search.alphabeta.pv.filters.ExtendPV;
import net.chesstango.search.alphabeta.pv.filters.PropagatePV;
import net.chesstango.search.alphabeta.quiescence.QSAlphaBeta;
import net.chesstango.search.alphabeta.quiescence.QSStandingPat;
import net.chesstango.search.alphabeta.statistics.node.filters.QuiescenceNodeExpected;
import net.chesstango.search.alphabeta.statistics.node.filters.QuiescenceNodeVisited;
import net.chesstango.search.alphabeta.transposition.filters.TranspositionTableQ;
import net.chesstango.search.alphabeta.zobrist.filters.ZobristTracker;
import net.chesstango.search.builders.sorters.MoveSorterQuiescenceBuilder;
import net.chesstango.search.sorters.MoveSorter;

import java.util.LinkedList;
import java.util.List;

import static net.chesstango.search.alphabeta.Constants.MAX_DEPTH;

/**
 * @author Mauricio Coria
 */
public class QuiescenceChainBuilder extends AbstractChainBuilder {
    private final MoveSorterQuiescenceBuilder moveSorterBuilder;
    private QSStandingPat qsStandingPat;
    private QSAlphaBeta qsAlphaBeta;
    private AlphaBetaFlowControl alphaBetaFlowControl;
    private QuiescenceNodeVisited quiescenceNodeVisited;
    private QuiescenceNodeExpected quiescenceNodeExpected;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private DebugFilter debugFilter;
    private PropagatePV propagatePV;
    private ExtendPV extendPV;
    private MoveSorter moveSorter;

    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTranspositionTable;
    private boolean withDebugSearchTree;
    private boolean withDeltaPruning;


    public QuiescenceChainBuilder() {
        moveSorterBuilder = new MoveSorterQuiescenceBuilder();
    }

    public QuiescenceChainBuilder withIterativeDeepening() {
        moveSorterBuilder.withIterativeDeepening();
        return this;
    }

    public QuiescenceChainBuilder withAlphaBetaFlowControl(AlphaBetaFlowControl alphaBetaFlowControl) {
        this.alphaBetaFlowControl = alphaBetaFlowControl;
        return this;
    }

    public QuiescenceChainBuilder withSmartListenerMediator(ListenerMediator listenerMediator) {
        this.listenerMediator = listenerMediator;
        this.moveSorterBuilder.withSmartListenerMediator(listenerMediator);
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

    public QuiescenceChainBuilder withDebugSearchTree() {
        moveSorterBuilder.withDebugSearchTree();
        this.withDebugSearchTree = true;
        return this;
    }

    public QuiescenceChainBuilder withGameEvaluatorCache() {
        moveSorterBuilder.withGameEvaluatorCache();
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

    public QuiescenceChainBuilder withDeltaPruning() {
        this.withDeltaPruning = true;
        return this;
    }

    @Override
    protected void buildObjects() {
        qsStandingPat = new QSStandingPat(withDeltaPruning);
        qsAlphaBeta = new QSAlphaBeta(withDeltaPruning);
        extendPV = new ExtendPV();
        propagatePV = new PropagatePV();

        if (withStatistics) {
            quiescenceNodeVisited = new QuiescenceNodeVisited();
            quiescenceNodeExpected = new QuiescenceNodeExpected();
        }

        if (withZobristTracker) {
            zobristQTracker = new ZobristTracker();
        }

        if (withTranspositionTable) {
            transpositionTableQ = new TranspositionTableQ();
        }

        if (withDebugSearchTree) {
            debugFilter = new DebugFilter(NodeTopology.QUIESCENCE);
        }

        moveSorter = moveSorterBuilder.build();
    }

    @Override
    protected void setupListenerMediator() {
        listenerMediator.add(qsStandingPat);
        listenerMediator.add(qsAlphaBeta);

        if (quiescenceNodeVisited != null) {
            listenerMediator.add(quiescenceNodeVisited);
        }

        if (quiescenceNodeExpected != null) {
            listenerMediator.add(quiescenceNodeExpected);
        }

        if (zobristQTracker != null) {
            listenerMediator.add(zobristQTracker);
        }

        if (transpositionTableQ != null) {
            listenerMediator.add(transpositionTableQ);
        }

        if (withDebugSearchTree) {
            listenerMediator.add(debugFilter);
        }

        if (extendPV != null) {
            listenerMediator.add(extendPV);
        }

        if (propagatePV != null) {
            listenerMediator.add(propagatePV);
        }
    }

    @Override
    public void link() {
        qsAlphaBeta.setMoveSorter(moveSorter);

        int[] standingPats = new int[MAX_DEPTH];

        qsStandingPat.setStandingPats(standingPats);
        qsAlphaBeta.setStandingPats(standingPats);
    }

    @Override
    protected AlphaBetaFilter buildAlphaBetaChain() {
        List<AlphaBetaFilter> chain = new LinkedList<>();

        if (debugFilter != null) {
            chain.add(debugFilter);
        }

        if (extendPV != null) {
            chain.add(extendPV);
        }

        if (zobristQTracker != null) {
            chain.add(zobristQTracker);
        }

        if (quiescenceNodeVisited != null) {
            chain.add(quiescenceNodeVisited);
        }

        if (transpositionTableQ != null) {
            chain.add(transpositionTableQ);
        }

        chain.add(qsStandingPat);

        /**
         * QuiescenceStandingPat puede superar beta, por lo cual no debemos incrementar expected
         */
        if (quiescenceNodeExpected != null) {
            chain.add(quiescenceNodeExpected);
        }

        chain.add(qsAlphaBeta);

        if (propagatePV != null) {
            chain.add(propagatePV);
        }

        chain.add(alphaBetaFlowControl);

        return createChain(chain);
    }

}
