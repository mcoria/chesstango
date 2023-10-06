package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;

import java.util.List;

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
    private List<SearchLifeCycle> filterActions;
    private boolean withQuiescence;
    private boolean withStatistics;
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
        withZobristTracker = true;
        return this;
    }

    public QuiescenceChainBuilder withFilterActions(List<SearchLifeCycle> filterActions) {
        this.filterActions = filterActions;
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

        createSearchActions();

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
            if (withZobristTracker) {
                zobristQTracker = new ZobristTracker();
            }
        } else {
            quiescenceNull = new QuiescenceNull();
        }
    }

    private List<SearchLifeCycle> createSearchActions() {
        // =============  quiescence setup =====================
        if (withQuiescence) {
            filterActions.add(qMoveSorter);
            filterActions.add(quiescence);
            filterActions.add(quiescenceFlowControl);
            if (withStatistics) {
                filterActions.add(quiescenceStatisticsExpected);
                filterActions.add(quiescenceStatisticsVisited);
            }
            if (zobristQTracker != null) {
                filterActions.add(zobristQTracker);
            }
            if (transpositionTableQ != null) {
                filterActions.add(transpositionTableQ);
            }
        } else {
            filterActions.add(quiescenceNull);
        }
        // ====================================================

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            filterActions.add(evaluatorStatistics);
        }

        return filterActions;
    }


    private AlphaBetaFilter createQuiescenceChain() {
        AlphaBetaFilter headQuiescence = null;

        if (withQuiescence) {
            if (zobristQTracker != null) {
                if (transpositionTableQ != null) {
                    zobristQTracker.setNext(transpositionTableQ);
                } else {
                    zobristQTracker.setNext(quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence);
                }
                headQuiescence = zobristQTracker;
            }

            if (transpositionTableQ != null) {
                transpositionTableQ.setNext(quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence);
                if (headQuiescence == null) {
                    headQuiescence = transpositionTableQ;
                }
            }

            if (headQuiescence == null) {
                headQuiescence = quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence;
            }

            if (quiescenceStatisticsExpected != null) {
                quiescenceStatisticsExpected.setNext(quiescence);
            }

            quiescence.setNext(quiescenceStatisticsVisited != null ? quiescenceStatisticsVisited : quiescenceFlowControl);
            quiescence.setMoveSorter(qMoveSorter);
            quiescence.setGameEvaluator(gameEvaluator);


            if (quiescenceStatisticsVisited != null) {
                quiescenceStatisticsVisited.setNext(quiescenceFlowControl);
            }

            quiescenceFlowControl.setNext(headQuiescence);
            quiescenceFlowControl.setGameEvaluator(gameEvaluator);

        } else {
            quiescenceNull.setGameEvaluator(gameEvaluator);
            headQuiescence = quiescenceNull;
        }

        return headQuiescence;
    }
}
