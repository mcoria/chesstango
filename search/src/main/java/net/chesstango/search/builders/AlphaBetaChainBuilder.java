package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListener;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;

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
    private TriangularPV triangularPV;
    private List<SmartListener> filterActions;
    private boolean withStatistics;
    private boolean withZobristTracker;
    private boolean withTriangularPV;

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

    public AlphaBetaChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public AlphaBetaChainBuilder withFilterActions(List<SmartListener> filterActions) {
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
        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
        alphaBeta.setMoveSorter(moveSorter);
    }

    private void createSearchActions() {
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
        if (withTriangularPV) {
            filterActions.add(triangularPV);
        }

    }


    private AlphaBetaFilter createChain() {

        // =============  alphaBeta setup =====================
        AlphaBetaFilter head = null;
        AlphaBetaFilter tail = null;
        if (zobristTracker != null) {
            head = zobristTracker;
            tail = zobristTracker;
        }


        if (alphaBetaStatisticsExpected != null) {
            if (head == null) {
                head = alphaBetaStatisticsExpected;
            }
            if (tail instanceof ZobristTracker zobristTrackerTail) {
                zobristTrackerTail.setNext(alphaBetaStatisticsExpected);
            } else if (tail instanceof TranspositionTable transpositionTableTail) {
                transpositionTableTail.setNext(alphaBetaStatisticsExpected);
            }
            tail = alphaBetaStatisticsExpected;
        }

        if (head == null) {
            head = alphaBeta;
        }
        if (tail instanceof ZobristTracker zobristTrackerTail) {
            zobristTrackerTail.setNext(alphaBeta);
        } else if (tail instanceof AlphaBetaStatisticsExpected alphaBetaStatisticsExpectedTail) {
            alphaBetaStatisticsExpectedTail.setNext(alphaBeta);
        }
        tail = alphaBeta;

        if (alphaBetaStatisticsVisited != null) {
            alphaBeta.setNext(alphaBetaStatisticsVisited);
            tail = alphaBetaStatisticsVisited;
        }

        if (triangularPV != null) {
            if (tail instanceof AlphaBeta) {
                alphaBeta.setNext(triangularPV);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(triangularPV);
            }
            tail = triangularPV;
        }


        if (transpositionTable != null) {
            if (tail instanceof AlphaBeta) {
                alphaBeta.setNext(transpositionTable);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(transpositionTable);
            } else if (tail instanceof TriangularPV) {
                triangularPV.setNext(transpositionTable);
            }
            tail = transpositionTable;
        }

        if (tail instanceof AlphaBeta) {
            alphaBeta.setNext(alphaBetaFlowControl);
        } else if (tail instanceof AlphaBetaStatisticsVisited) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        } else if (tail instanceof TriangularPV) {
            triangularPV.setNext(alphaBetaFlowControl);
        } else if (tail instanceof TranspositionTable) {
            transpositionTable.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(head);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);


        return head;
    }
}
