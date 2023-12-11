package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

/**
 * @author Mauricio Coria
 */
public class QuiescenceChainBuilder {
    private GameEvaluator gameEvaluator;
    private MoveSorter qMoveSorter;
    private QuiescenceNull quiescenceNull;
    private Quiescence quiescence;
    private QuiescenceFlowControl quiescenceFlowControl;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTableQ transpositionTableQ;
    private ZobristTracker zobristQTracker;
    private TriangularPV triangularPV;
    private SmartListenerMediator smartListenerMediator;
    private boolean withQuiescence;
    private boolean withStatistics;
    private boolean withTriangularPV;
    private boolean withZobristTracker;


    public QuiescenceChainBuilder() {
        qMoveSorter = new DefaultMoveSorter();
    }

    public QuiescenceChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public QuiescenceChainBuilder withQuiescence() {
        this.withQuiescence = true;
        return this;
    }

    public QuiescenceChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public QuiescenceChainBuilder withTranspositionTable() {
        if (!withQuiescence) {
            throw new RuntimeException("You must enable Quiescence first");
        }
        transpositionTableQ = new TranspositionTableQ();
        return this;
    }

    public QuiescenceChainBuilder withTranspositionMoveSorter() {
        if (transpositionTableQ == null) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        qMoveSorter = new TranspositionMoveSorterQ();
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

    public QuiescenceChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
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

        return createQuiescenceChain();
    }

    private void buildObjects() {
        if (withQuiescence) {
            quiescence = new Quiescence();
            quiescenceFlowControl = new QuiescenceFlowControl();
            if (withStatistics) {
                quiescenceStatisticsExpected = new QuiescenceStatisticsExpected();
                quiescenceStatisticsVisited = new QuiescenceStatisticsVisited();
            }
            if (withTriangularPV) {
                triangularPV = new TriangularPV();
            }
            if (withZobristTracker) {
                zobristQTracker = new ZobristTracker();
            }
        } else {
            quiescenceNull = new QuiescenceNull();
        }
    }

    private void setupListenerMediator() {
        // =============  quiescence setup =====================
        if (withQuiescence) {
            smartListenerMediator.add(qMoveSorter);
            smartListenerMediator.add(quiescence);
            smartListenerMediator.add(quiescenceFlowControl);
            if (withStatistics) {
                smartListenerMediator.add(quiescenceStatisticsExpected);
                smartListenerMediator.add(quiescenceStatisticsVisited);
            }
            if (zobristQTracker != null) {
                smartListenerMediator.add(zobristQTracker);
            }
            if (transpositionTableQ != null) {
                smartListenerMediator.add(transpositionTableQ);
            }
            if (withTriangularPV) {
                smartListenerMediator.add(triangularPV);
            }
        } else {
            smartListenerMediator.add(quiescenceNull);
        }
        // ====================================================

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            smartListenerMediator.add(evaluatorStatistics);
        }

    }


    private AlphaBetaFilter createQuiescenceChain() {
        AlphaBetaFilter headQuiescence = null;

        if (withQuiescence) {
            quiescence.setMoveSorter(qMoveSorter);
            quiescence.setGameEvaluator(gameEvaluator);
            headQuiescence = createRealQuiescenceChain();
        } else {
            quiescenceNull.setGameEvaluator(gameEvaluator);
            headQuiescence = quiescenceNull;
        }

        return headQuiescence;
    }

    private AlphaBetaFilter createRealQuiescenceChain() {
        AlphaBetaFilter head = null;
        AlphaBetaFilter tail = null;


        if (quiescenceStatisticsExpected != null) {
            head = quiescenceStatisticsExpected;
            tail = quiescenceStatisticsExpected;
        }

        if (head == null) {
            head = quiescence;
        } else if (tail instanceof QuiescenceStatisticsExpected) {
            quiescenceStatisticsExpected.setNext(quiescence);
        }
        tail = quiescence;

        if (zobristQTracker != null) {
            quiescence.setNext(zobristQTracker);
            tail = zobristQTracker;
        }

        if (quiescenceStatisticsVisited != null) {
            if (tail instanceof Quiescence) {
                quiescence.setNext(quiescenceStatisticsVisited);
            } else if (tail instanceof ZobristTracker) {
                zobristQTracker.setNext(quiescenceStatisticsVisited);
            }
            tail = quiescenceStatisticsVisited;
        }

        if (triangularPV != null) {
            if (tail instanceof Quiescence) {
                quiescence.setNext(triangularPV);
            } else if (tail instanceof ZobristTracker) {
                zobristQTracker.setNext(triangularPV);
            } else if (tail instanceof QuiescenceStatisticsVisited) {
                quiescenceStatisticsVisited.setNext(triangularPV);
            }
            tail = triangularPV;
        }

        if (transpositionTableQ != null) {
            if (tail instanceof Quiescence) {
                quiescence.setNext(transpositionTableQ);
            } else if (tail instanceof ZobristTracker) {
                zobristQTracker.setNext(transpositionTableQ);
            } else if (tail instanceof QuiescenceStatisticsVisited) {
                quiescenceStatisticsVisited.setNext(transpositionTableQ);
            } else if (tail instanceof TriangularPV) {
                triangularPV.setNext(transpositionTableQ);
            }
            tail = transpositionTableQ;
        }

        if (tail instanceof Quiescence) {
            quiescence.setNext(quiescenceFlowControl);
        } else if (tail instanceof ZobristTracker) {
            zobristQTracker.setNext(quiescenceFlowControl);
        } else if (tail instanceof QuiescenceStatisticsVisited) {
            quiescenceStatisticsVisited.setNext(quiescenceFlowControl);
        } else if (tail instanceof TriangularPV) {
            triangularPV.setNext(quiescenceFlowControl);
        } else if (tail instanceof TranspositionTableQ) {
            transpositionTableQ.setNext(quiescenceFlowControl);
        }

        quiescenceFlowControl.setNext(head);
        quiescenceFlowControl.setGameEvaluator(gameEvaluator);

        return head;
    }
}
