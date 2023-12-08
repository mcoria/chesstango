package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SmartListener;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.*;

import java.util.List;

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

    private List<SmartListener> filterActions;

    private boolean withStatistics;

    private boolean withAspirationWindows;
    private boolean withTranspositionTable;
    private boolean withTriangularPV;

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

    public AlphaBetaFirstChainBuilder withFilterActions(List<SmartListener> searchActions) {
        this.filterActions = searchActions;
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

    public AlphaBetaFilter build() {
        buildObjects();

        addSearchLifeCycleListeners();

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

        moveEvaluationTracker = new MoveEvaluationTracker();
    }


    private void addSearchLifeCycleListeners() {
        filterActions.add(alphaBetaRoot);
        filterActions.add(moveEvaluationTracker);
        filterActions.add(alphaBetaFlowControl);


        if (withStatistics) {
            filterActions.add(alphaBetaStatisticsExpected);
            filterActions.add(alphaBetaStatisticsVisited);
        }

        if (aspirationWindows != null) {
            filterActions.add(aspirationWindows);
        }

        if (stopProcessingCatch != null) {
            filterActions.add(stopProcessingCatch);
        }

        if (withTranspositionTable) {
            filterActions.add(transpositionTableRoot);
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

        if (stopProcessingCatch != null) {
            head = stopProcessingCatch;
            tail = stopProcessingCatch;
        }

        if (transpositionTableRoot != null) {
            if (head == null) {
                head = transpositionTableRoot;
            }
            if (tail instanceof StopProcessingCatch stopProcessingCatchTail) {
                stopProcessingCatchTail.setNext(transpositionTableRoot);
            }
            tail = transpositionTableRoot;
        }

        if (aspirationWindows != null) {
            if (head == null) {
                head = aspirationWindows;
            }
            aspirationWindows.setMoveEvaluationTracker(moveEvaluationTracker);
            if (tail instanceof StopProcessingCatch stopProcessingCatchTail) {
                stopProcessingCatchTail.setNext(aspirationWindows);
            } else if (tail instanceof TranspositionTableRoot transpositionTableRootTail) {
                transpositionTableRootTail.setNext(aspirationWindows);
            }
            tail = aspirationWindows;
        }

        if (alphaBetaStatisticsExpected != null) {
            if (head == null) {
                head = alphaBetaStatisticsExpected;
            }
            if (tail instanceof StopProcessingCatch stopProcessingCatchTail) {
                stopProcessingCatchTail.setNext(alphaBetaStatisticsExpected);
            } else if (tail instanceof TranspositionTableRoot transpositionTableRootTail) {
                transpositionTableRootTail.setNext(alphaBetaStatisticsExpected);
            } else if (tail instanceof AspirationWindows aspirationWindowsTail) {
                aspirationWindowsTail.setNext(alphaBetaStatisticsExpected);
            }
            tail = alphaBetaStatisticsExpected;
        }

        if (head == null) {
            head = alphaBetaRoot;
        }
        if (tail instanceof StopProcessingCatch stopProcessingCatchTail) {
            stopProcessingCatchTail.setNext(alphaBetaRoot);
        } else if (tail instanceof TranspositionTableRoot transpositionTableRootTail) {
            transpositionTableRootTail.setNext(alphaBetaRoot);
        } else if (tail instanceof AspirationWindows aspirationWindowsTail) {
            aspirationWindowsTail.setNext(alphaBetaRoot);
        } else if (tail instanceof AlphaBetaStatisticsExpected alphaBetaStatisticsExpectedTail) {
            alphaBetaStatisticsExpectedTail.setNext(alphaBetaRoot);
        }

        alphaBetaRoot.setNext(moveEvaluationTracker);
        moveEvaluationTracker.setStopProcessingCatch(stopProcessingCatch);
        tail = moveEvaluationTracker;

        if (alphaBetaStatisticsVisited != null) {
            moveEvaluationTracker.setNext(alphaBetaStatisticsVisited);
            tail = alphaBetaStatisticsVisited;
        }

        if (triangularPV != null) {
            if (tail instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(triangularPV);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(triangularPV);
            }
            tail = triangularPV;
        }

        if (transpositionTable != null) {
            if (tail instanceof MoveEvaluationTracker) {
                moveEvaluationTracker.setNext(transpositionTable);
            } else if (tail instanceof AlphaBetaStatisticsVisited) {
                alphaBetaStatisticsVisited.setNext(transpositionTable);
            } else if (tail instanceof TriangularPV) {
                triangularPV.setNext(transpositionTable);
            }
            tail = transpositionTable;
        }

        if (tail instanceof MoveEvaluationTracker) {
            moveEvaluationTracker.setNext(alphaBetaFlowControl);
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
