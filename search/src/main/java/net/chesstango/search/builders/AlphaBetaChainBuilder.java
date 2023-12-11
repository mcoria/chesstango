package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;

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
    private SmartListenerMediator smartListenerMediator;
    private boolean withStatistics;
    private boolean withTriangularPV;
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
        this.withZobristTracker = true;
        return this;
    }

    public AlphaBetaChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public AlphaBetaChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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

        setupListenerMediator();

        return createChain();
    }

    private void buildObjects() {
        if (withStatistics) {
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }
        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }
        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        alphaBeta.setMoveSorter(moveSorter);
    }

    private void setupListenerMediator() {
        smartListenerMediator.add(moveSorter);
        smartListenerMediator.add(alphaBeta);
        smartListenerMediator.add(alphaBetaFlowControl);

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            smartListenerMediator.add(alphaBetaStatisticsExpected);
            smartListenerMediator.add(alphaBetaStatisticsVisited);
        }
        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }
        if (transpositionTable != null) {
            smartListenerMediator.add(transpositionTable);
        }
        if (withTriangularPV) {
            smartListenerMediator.add(triangularPV);
        }
    }


    private AlphaBetaFilter createChain() {

        // =============  alphaBeta setup =====================
        AlphaBetaFilter head = null;
        AlphaBetaFilter tail = null;

        if (alphaBetaStatisticsExpected != null) {
            head = alphaBetaStatisticsExpected;
            tail = alphaBetaStatisticsExpected;
        }

        if (head == null) {
            head = alphaBeta;
        } else if (tail instanceof AlphaBetaStatisticsExpected) {
            alphaBetaStatisticsExpected.setNext(alphaBeta);
        }

        tail = alphaBeta;


        if (zobristTracker != null) {
            alphaBeta.setNext(zobristTracker);
            tail = zobristTracker;
        }

        if (alphaBetaStatisticsVisited != null) {
            if (tail instanceof AlphaBeta) {
                alphaBeta.setNext(alphaBetaStatisticsVisited);
            } else if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(alphaBetaStatisticsVisited);
            }
            tail = alphaBetaStatisticsVisited;
        }

        if (triangularPV != null) {
            if (tail instanceof AlphaBeta) {
                alphaBeta.setNext(triangularPV);
            } else if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(triangularPV);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(triangularPV);
            }
            tail = triangularPV;
        }


        if (transpositionTable != null) {
            if (tail instanceof AlphaBeta) {
                alphaBeta.setNext(transpositionTable);
            } else if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(transpositionTable);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(transpositionTable);
            } else if (tail instanceof TriangularPV) {
                triangularPV.setNext(transpositionTable);
            }
            tail = transpositionTable;
        }

        if (tail instanceof AlphaBeta) {
            alphaBeta.setNext(alphaBetaFlowControl);
        } else if (tail instanceof ZobristTracker) {
            zobristTracker.setNext(alphaBetaFlowControl);
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
