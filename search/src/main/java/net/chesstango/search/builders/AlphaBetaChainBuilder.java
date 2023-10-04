package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaChainBuilder {
    private final AlphaBeta alphaBeta;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private GameEvaluator gameEvaluator;
    private MoveSorter moveSorter;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private TranspositionTable transpositionTable;
    private ZobristTracker zobristTracker;

    private AlphaBetaFilter quiescence;
    private List<SearchLifeCycle> filterActions;
    private boolean withStatistics;
    private boolean withZobristTracker;


    public AlphaBetaChainBuilder() {
        alphaBeta = new AlphaBeta();

        moveSorter = new DefaultMoveSorter();

        alphaBetaFlowControl = new AlphaBetaFlowControl();
    }

    public AlphaBetaChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public AlphaBetaChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaChainBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }


    public AlphaBetaChainBuilder withTranspositionMoveSorter() {
        if (transpositionTable == null) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        moveSorter = new TranspositionMoveSorter();
        return this;
    }

    public AlphaBetaChainBuilder withZobristTracker() {
        withZobristTracker = true;
        return this;
    }

    public AlphaBetaChainBuilder withFilterActions(List<SearchLifeCycle> filterActions) {
        this.filterActions = filterActions;
        return this;
    }

    public AlphaBetaChainBuilder withQuiescence(AlphaBetaFilter quiescence) {
        this.quiescence = quiescence;
        return this;
    }


    /**
     * AlphaBetaFacade -> StopProcessingCatch ->
     * *
     * * AlphaBetaStatistics -> ZobristTracker -> TranspositionTable -> AlphaBetaFlowControl -> AlphaBeta
     * *            ^                                                                            |
     * *            |                                                                            |
     * *            -----------------------------------------------------------------------------
     * *
     * StopProcessingCatch: al comienzo y solo una vez para atrapar excepciones de stop
     * AlphaBetaStatistics: al comienzo, para contabilizar los movimientos iniciales posibles
     * TranspositionTable: al comienzo, con iterative deeping tiene sentido dado que se puede repetir la misma busqueda
     *
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

        createSearchActions();

        return createChain();
    }

    private void buildObjects() {
        // =============  alphaBeta setup =====================
        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
        // ====================================================

    }

    private List<SearchLifeCycle> createSearchActions() {
        filterActions.add(moveSorter);
        filterActions.add(alphaBeta);
        filterActions.add(alphaBetaFlowControl);

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            filterActions.add(alphaBetaStatisticsExpected);
            filterActions.add(alphaBetaStatisticsVisited);
        }

        if (zobristTracker != null) {
            filterActions.add(zobristTracker);
        }

        if (transpositionTable != null) {
            filterActions.add(transpositionTable);
        }

        return filterActions;
    }


    private AlphaBetaFilter createChain() {

        // =============  alphaBeta setup =====================
        AlphaBetaFilter head = null;
        if (zobristTracker != null) {
            if (transpositionTable != null) {
                zobristTracker.setNext(transpositionTable);
            } else {
                zobristTracker.setNext(alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta);
            }
            head = zobristTracker;
        }

        if (transpositionTable != null) {
            transpositionTable.setNext(alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta;
        }


        if (alphaBetaStatisticsExpected != null) {
            alphaBetaStatisticsExpected.setNext(alphaBeta);
        }

        alphaBeta.setNext(alphaBetaStatisticsVisited != null ? alphaBetaStatisticsVisited : alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);

        if (alphaBetaStatisticsVisited != null) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(head);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        // ====================================================


        return head;
    }
}
