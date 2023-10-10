package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.EvaluatorStatistics;
import net.chesstango.search.smart.alphabeta.listeners.*;
import net.chesstango.search.smart.statistics.GameStatisticsListener;
import net.chesstango.search.smart.statistics.IterativeWrapper;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaBuilder implements SearchBuilder {
    private final AlphaBetaFirstChainBuilder alphaBetaFirstChainBuilder;
    private final AlphaBetaChainBuilder alphaBetaChainBuilder;
    private final QuiescenceChainBuilder quiescenceChainBuilder;
    private GameEvaluator gameEvaluator;
    private SetTranspositionTables setTranspositionTables;
    private SetPrincipalVariation setPrincipalVariation;

    private SetNodeStatistics setNodeStatistics;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;


    public AlphaBetaBuilder() {
        alphaBetaFirstChainBuilder = new AlphaBetaFirstChainBuilder();
        alphaBetaChainBuilder = new AlphaBetaChainBuilder();
        quiescenceChainBuilder = new QuiescenceChainBuilder();
    }

    public AlphaBetaBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }


    @Override
    public AlphaBetaBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public AlphaBetaBuilder withQuiescence() {
        quiescenceChainBuilder.withQuiescence();
        return this;
    }

    public AlphaBetaBuilder withStatistics() {
        this.withStatistics = true;
        quiescenceChainBuilder.withStatistics();
        alphaBetaChainBuilder.withStatistics();
        alphaBetaFirstChainBuilder.withStatistics();
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        withTranspositionTable = true;
        alphaBetaChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionTable() {
        quiescenceChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        alphaBetaChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionMoveSorter() {
        quiescenceChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withStopProcessingCatch() {
        alphaBetaFirstChainBuilder.withStopProcessingCatch();
        return this;
    }


    public AlphaBetaBuilder withTranspositionTableReuse() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        withTranspositionTableReuse = true;
        return this;
    }


    public AlphaBetaBuilder withTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable Statistics first");
        }
        withTrackEvaluations = true;
        return this;
    }


    public AlphaBetaBuilder withZobristTracker() {
        alphaBetaChainBuilder.withZobristTracker();
        return this;
    }

    public AlphaBetaBuilder withAspirationWindows() {
        alphaBetaFirstChainBuilder.withAspirationWindows();
        return this;
    }


    @Override
    public SearchMove build() {
        buildObjects();

        List<SearchLifeCycle> searchActions = createSearchActions();

        AlphaBetaFilter head = createChain(searchActions);

        // ====================================================
        AlphaBetaFacade alphaBetaFacade = new AlphaBetaFacade();
        alphaBetaFacade.setAlphaBetaSearch(head);
        alphaBetaFacade.setSearchActions(searchActions);

        SearchMove searchMove;

        if (withIterativeDeepening) {
            searchMove = new IterativeDeepening(alphaBetaFacade);
        } else {
            searchMove = new NoIterativeDeepening(alphaBetaFacade);
        }

        if (withStatistics) {
            IterativeWrapper iterativeWrapper = new IterativeWrapper(searchMove);

            searchActions.add(new GameStatisticsListener());

            searchMove = iterativeWrapper;
        }

        return searchMove;
    }

    private void buildObjects() {
        if (withStatistics) {
            gameEvaluator = new EvaluatorStatistics(gameEvaluator)
                    .setTrackEvaluations(withTrackEvaluations);
        }

        if (withTranspositionTable) {
            setTranspositionTables = new SetTranspositionTables();
            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }
        }

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            setNodeStatistics = new SetNodeStatistics();
        }

        setPrincipalVariation = new SetPrincipalVariation();
    }


    private List<SearchLifeCycle> createSearchActions() {
        List<SearchLifeCycle> filterActions = new LinkedList<>();

        if (setTranspositionTables != null) {
            // Este filtro necesita agregarse primero
            filterActions.add(setTranspositionTables);
        }

        if (withStatistics) {
            filterActions.add(setNodeStatistics);

        }

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            filterActions.add(evaluatorStatistics);
        }

        filterActions.add(setPrincipalVariation);

        return filterActions;
    }


    private AlphaBetaFilter createChain(List<SearchLifeCycle> searchActions) {
        quiescenceChainBuilder.withFilterActions(searchActions);
        quiescenceChainBuilder.withGameEvaluator(gameEvaluator);
        AlphaBetaFilter quiescenceChain = quiescenceChainBuilder.build();


        alphaBetaChainBuilder.withFilterActions(searchActions);
        alphaBetaChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaChainBuilder.withQuiescence(quiescenceChain);
        AlphaBetaFilter alphaBetaChain = alphaBetaChainBuilder.build();

        alphaBetaFirstChainBuilder.withFilterActions(searchActions);
        alphaBetaFirstChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaFirstChainBuilder.withNext(alphaBetaChain);
        alphaBetaFirstChainBuilder.withQuiescence(quiescenceChain);
        AlphaBetaFilter alphaBetaFirstChain = alphaBetaFirstChainBuilder.build();

        return alphaBetaFirstChain;
    }


}
