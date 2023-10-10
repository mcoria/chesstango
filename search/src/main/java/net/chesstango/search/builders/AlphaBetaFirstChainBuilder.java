package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatisticsExpected;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatisticsVisited;
import net.chesstango.search.smart.alphabeta.filters.once.AlphaBetaFirst;
import net.chesstango.search.smart.alphabeta.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.filters.once.MoveTracker;
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
    private AspirationWindows aspirationWindows;
    private MoveTracker moveTracker;

    private List<SearchLifeCycle> filterActions;

    private boolean withStatistics;

    private boolean withAspirationWindows;

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

    public AlphaBetaFirstChainBuilder withAspirationWindows() {
        this.withAspirationWindows = true;
        return this;
    }

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

        if (withAspirationWindows) {
            aspirationWindows = new AspirationWindows();
        }

        moveTracker = new MoveTracker();
    }


    private List<SearchLifeCycle> createSearchActions() {
        filterActions.add(alphaBetaFirst);
        filterActions.add(alphaBetaFlowControl);
        filterActions.add(moveTracker);

        if (withStatistics) {
            filterActions.add(alphaBetaStatisticsExpected);
            filterActions.add(alphaBetaStatisticsVisited);
        }

        if (stopProcessingCatch != null) {
            filterActions.add(stopProcessingCatch);
        }

        if (aspirationWindows != null) {
            filterActions.add(aspirationWindows);
        }

        return filterActions;
    }


    private AlphaBetaFilter createChain() {
        // =============  alphaBeta setup =====================

        AlphaBetaFilter head = alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBetaFirst;


        if (alphaBetaStatisticsExpected != null) {
            alphaBetaStatisticsExpected.setNext(alphaBetaFirst);
        }

        alphaBetaFirst.setNext(moveTracker);

        moveTracker.setNext(alphaBetaStatisticsVisited != null ? alphaBetaStatisticsVisited : alphaBetaFlowControl);
        moveTracker.setAlphaBetaFirst(alphaBetaFirst);

        if (alphaBetaStatisticsVisited != null) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(next);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        // ====================================================

        if (aspirationWindows != null) {
            aspirationWindows.setNext(head);
            head = aspirationWindows;
        }

        // StopProcessingCatch is set one in the chain
        if (stopProcessingCatch != null) {
            stopProcessingCatch.setNext(head);
            stopProcessingCatch.setAlphaBetaFirst(alphaBetaFirst);
            head = stopProcessingCatch;
        }

        return head;
    }

}
