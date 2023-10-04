package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.alphabeta.filters.once.AlphaBetaFirst;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaFirstChainBuilder {
    private final AlphaBetaFirst alphaBetaFirst;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private GameEvaluator gameEvaluator;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private StopProcessingCatch stopProcessingCatch;
    private AlphaBetaFilter next;
    private AlphaBetaFilter quiescence;

    private List<SearchLifeCycle> filterActions;

    private boolean withStatistics;

    public AlphaBetaFirstChainBuilder() {
        alphaBetaFirst = new AlphaBetaFirst();

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

    public AlphaBetaFirstChainBuilder withFilterActions(List<SearchLifeCycle> searchActions) {
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
        // ====================================================
    }


    private List<SearchLifeCycle> createSearchActions() {
        filterActions.add(alphaBetaFirst);
        filterActions.add(alphaBetaFlowControl);

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            filterActions.add(alphaBetaStatisticsExpected);
            filterActions.add(alphaBetaStatisticsVisited);
        }

        if (stopProcessingCatch != null) {
            filterActions.add(stopProcessingCatch);
        }
        // ====================================================
        return filterActions;
    }


    private AlphaBetaFilter createChain() {
        // =============  alphaBeta setup =====================

        AlphaBetaFilter head = alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBetaFirst;


        if (alphaBetaStatisticsExpected != null) {
            alphaBetaStatisticsExpected.setNext(alphaBetaFirst);
        }

        alphaBetaFirst.setNext(alphaBetaStatisticsVisited != null ? alphaBetaStatisticsVisited : alphaBetaFlowControl);

        if (alphaBetaStatisticsVisited != null) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(next);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        // ====================================================

        // StopProcessingCatch is set one in the chain
        if (stopProcessingCatch != null) {
            stopProcessingCatch.setNext(head);
            head = stopProcessingCatch;
        }

        return head;
    }

}
