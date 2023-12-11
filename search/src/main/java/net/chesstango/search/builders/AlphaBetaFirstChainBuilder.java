package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.*;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFirstChainBuilder {
    private final AlphaBetaRoot alphaBetaRoot;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private GameEvaluator gameEvaluator;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AlphaBetaFilter next;
    private AlphaBetaFilter quiescence;
    private AspirationWindows aspirationWindows;
    private MoveEvaluationTracker moveEvaluationTracker;
    private TranspositionTableRoot transpositionTableRoot;
    private TranspositionTable transpositionTable;
    private TriangularPV triangularPV;
    private SmartListenerMediator smartListenerMediator;
    private ZobristTracker zobristTracker;

    private boolean withStatistics;
    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withTriangularPV;
    private boolean withZobristTracker;

    public AlphaBetaFirstChainBuilder() {
        alphaBetaRoot = new AlphaBetaRoot();

        alphaBetaFlowControl = new AlphaBetaFlowControl();
    }


    public AlphaBetaFirstChainBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public AlphaBetaFirstChainBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaFirstChainBuilder withSmartListenerMediator(SmartListenerMediator smartListenerMediator) {
        this.smartListenerMediator = smartListenerMediator;
        return this;
    }

    public AlphaBetaFirstChainBuilder withStopProcessingCatch() {
        stopProcessingCatch = new StopProcessingCatch();
        return this;
    }


    public AlphaBetaFirstChainBuilder withNext(AlphaBetaFilter next) {
        this.next = next;
        return this;
    }

    public AlphaBetaFirstChainBuilder withQuiescence(AlphaBetaFilter quiescence) {
        this.quiescence = quiescence;
        return this;
    }

    public AlphaBetaFirstChainBuilder withAspirationWindows() {
        this.withAspirationWindows = true;
        return this;
    }

    public AlphaBetaFirstChainBuilder withTranspositionTable() {
        this.withTranspositionTable = true;
        return this;
    }

    public AlphaBetaFirstChainBuilder withTriangularPV() {
        this.withTriangularPV = true;
        return this;
    }

    public AlphaBetaFirstChainBuilder withZobristTracker() {
        this.withZobristTracker = true;
        return this;
    }

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

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
        }

        if (withTranspositionTable) {
            transpositionTableRoot = new TranspositionTableRoot();
            transpositionTable = new TranspositionTable();
        }

        if (withTriangularPV) {
            triangularPV = new TriangularPV();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }

        moveEvaluationTracker = new MoveEvaluationTracker();
        moveEvaluationTracker.setStopProcessingCatch(stopProcessingCatch);
    }


    private void setupListenerMediator() {
        smartListenerMediator.add(alphaBetaRoot);
        smartListenerMediator.add(moveEvaluationTracker);
        smartListenerMediator.add(alphaBetaFlowControl);


        if (withStatistics) {
            smartListenerMediator.add(alphaBetaStatisticsExpected);
            smartListenerMediator.add(alphaBetaStatisticsVisited);
        }

        if (aspirationWindows != null) {
            smartListenerMediator.add(aspirationWindows);
        }

        if (stopProcessingCatch != null) {
            smartListenerMediator.add(stopProcessingCatch);
        }

        if (triangularPV != null) {
            smartListenerMediator.add(triangularPV);
        }

        if (zobristTracker != null) {
            smartListenerMediator.add(zobristTracker);
        }

        if (withTranspositionTable) {
            smartListenerMediator.add(transpositionTableRoot);
            smartListenerMediator.add(transpositionTable);
        }
    }


    private AlphaBetaFilter createChain() {
        // =============  alphaBeta setup =====================

        AlphaBetaFilter head = null;
        AlphaBetaFilter tail = null;

        if (stopProcessingCatch != null) {
            head = stopProcessingCatch;
            tail = stopProcessingCatch;
        }

        if (transpositionTableRoot != null) {
            if (head == null) {
                head = transpositionTableRoot;
            }
            if (tail instanceof StopProcessingCatch) {
                stopProcessingCatch.setNext(transpositionTableRoot);
            }
            tail = transpositionTableRoot;
        }

        if (aspirationWindows != null) {
            if (head == null) {
                head = aspirationWindows;
            }
            aspirationWindows.setMoveEvaluationTracker(moveEvaluationTracker);
            if (tail instanceof StopProcessingCatch) {
                stopProcessingCatch.setNext(aspirationWindows);
            } else if (tail instanceof TranspositionTableRoot) {
                transpositionTableRoot.setNext(aspirationWindows);
            }
            tail = aspirationWindows;
        }

        if (alphaBetaStatisticsExpected != null) {
            if (head == null) {
                head = alphaBetaStatisticsExpected;
            }
            if (tail instanceof StopProcessingCatch) {
                stopProcessingCatch.setNext(alphaBetaStatisticsExpected);
            } else if (tail instanceof TranspositionTableRoot) {
                transpositionTableRoot.setNext(alphaBetaStatisticsExpected);
            } else if (tail instanceof AspirationWindows) {
                aspirationWindows.setNext(alphaBetaStatisticsExpected);
            }
            tail = alphaBetaStatisticsExpected;
        }

        if (head == null) {
            head = alphaBetaRoot;
        } else if (tail instanceof StopProcessingCatch) {
            stopProcessingCatch.setNext(alphaBetaRoot);
        } else if (tail instanceof TranspositionTableRoot) {
            transpositionTableRoot.setNext(alphaBetaRoot);
        } else if (tail instanceof AspirationWindows) {
            aspirationWindows.setNext(alphaBetaRoot);
        } else if (tail instanceof AlphaBetaStatisticsExpected) {
            alphaBetaStatisticsExpected.setNext(alphaBetaRoot);
        }

        alphaBetaRoot.setNext(moveEvaluationTracker);
        tail = moveEvaluationTracker;

        if (zobristTracker != null) {
            moveEvaluationTracker.setNext(zobristTracker);
            tail = zobristTracker;
        }

        if (alphaBetaStatisticsVisited != null) {
            if (tail instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(alphaBetaStatisticsVisited);
            } else if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(alphaBetaStatisticsVisited);
            }
            tail = alphaBetaStatisticsVisited;
        }

        if (triangularPV != null) {
            if (tail instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(triangularPV);
            } else if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(triangularPV);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(triangularPV);
            }
            tail = triangularPV;
        }

        if (transpositionTable != null) {
            if (tail instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(transpositionTable);
            }
            if (tail instanceof ZobristTracker) {
                zobristTracker.setNext(transpositionTable);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(transpositionTable);
            } else if (tail instanceof TriangularPV) {
                triangularPV.setNext(transpositionTable);
            }
            tail = transpositionTable;
        }

        if (tail instanceof MoveEvaluationTracker) {
            moveEvaluationTracker.setNext(alphaBetaFlowControl);
        }
        if (tail instanceof ZobristTracker) {
            zobristTracker.setNext(alphaBetaFlowControl);
        } else if (tail instanceof AlphaBetaStatisticsVisited) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        } else if (tail instanceof TriangularPV) {
            triangularPV.setNext(alphaBetaFlowControl);
        } else if (tail instanceof TranspositionTable) {
            transpositionTable.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(next);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        // ====================================================

        return head;
    }
}
