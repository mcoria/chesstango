package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListener;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.EvaluatorStatistics;
import net.chesstango.search.smart.alphabeta.listeners.*;
import net.chesstango.search.smart.statistics.GameStatisticsCycleListener;
import net.chesstango.search.smart.statistics.SearchMoveWrapper;

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
    private SetTranspositionPV setTranspositionPV;
    private SetBestMoves setBestMoves;
    private SetNodeStatistics setNodeStatistics;
    private GameStatisticsCycleListener gameStatisticsListener;
    private SetupGameEvaluator setupGameEvaluator;
    private SetTrianglePV setTrianglePV;
    private SmartListenerMediator smartListenerMediator;
    private AlphaBetaFacade alphaBetaFacade;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withTriangularPV;


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

    @Override
    public AlphaBetaBuilder withGameEvaluatorCache() {
        this.withGameEvaluatorCache = true;
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
        alphaBetaFirstChainBuilder.withTranspositionTable();
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

    public AlphaBetaBuilder withTriangularPV() {
        withTriangularPV = true;
        alphaBetaFirstChainBuilder.withTriangularPV();
        alphaBetaChainBuilder.withTriangularPV();
        quiescenceChainBuilder.withTriangularPV();
        return this;
    }

    @Override
    public SearchMove build() {
        buildObjects();

        setupListenerMediator();

        AlphaBetaFilter head = createChain();

        // ====================================================
        alphaBetaFacade = new AlphaBetaFacade();
        alphaBetaFacade.setAlphaBetaFilter(head);
        alphaBetaFacade.setSmartListenerMediator(smartListenerMediator);

        SearchMove searchMove;

        if (withIterativeDeepening) {
            searchMove = new IterativeDeepening(alphaBetaFacade);
        } else {
            searchMove = new NoIterativeDeepening(alphaBetaFacade);
        }

        if (withStatistics) {
            searchMove = new SearchMoveWrapper(searchMove);
        }

        return searchMove;
    }

    private void buildObjects() {
        smartListenerMediator = new SmartListenerMediator();

        if (withGameEvaluatorCache) {
            gameEvaluator = new GameEvaluatorCache(gameEvaluator);
        }

        if (withStatistics) {
            gameEvaluator = new EvaluatorStatistics(gameEvaluator).setTrackEvaluations(withTrackEvaluations);
            gameStatisticsListener = new GameStatisticsCycleListener();
        }

        if (withTranspositionTable) {
            setTranspositionTables = new SetTranspositionTables();
            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }
            setTranspositionPV = new SetTranspositionPV();
        }

        if (withTriangularPV) {
            setTrianglePV = new SetTrianglePV();
            setTrianglePV.setGameEvaluator(gameEvaluator);
        }

        if (withStatistics) {
            setNodeStatistics = new SetNodeStatistics();
        }

        setBestMoves = new SetBestMoves();

        setupGameEvaluator = new SetupGameEvaluator();

        alphaBetaFacade = new AlphaBetaFacade();
        alphaBetaFacade.setSmartListenerMediator(smartListenerMediator);
    }


    private void setupListenerMediator() {
        if (setTranspositionTables != null) {
            // Este filtro necesita agregarse primero
            smartListenerMediator.add(setTranspositionTables);
        }

        if (setTranspositionPV != null) {
            smartListenerMediator.add(setTranspositionPV);
        }

        if (setTrianglePV != null) {
            smartListenerMediator.add(setTrianglePV);
        }

        if (withStatistics) {
            smartListenerMediator.add(setNodeStatistics);
            smartListenerMediator.add(gameStatisticsListener);
        }

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            smartListenerMediator.add(evaluatorStatistics);
        }

        smartListenerMediator.add(setBestMoves);

        smartListenerMediator.add(setupGameEvaluator);
    }


    private AlphaBetaFilter createChain() {
        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        quiescenceChainBuilder.withSmartListenerMediator(smartListenerMediator);
        quiescenceChainBuilder.withGameEvaluator(gameEvaluator);
        AlphaBetaFilter quiescenceChain = quiescenceChainBuilder.build();


        alphaBetaChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaChainBuilder.withQuiescence(quiescenceChain);
        AlphaBetaFilter alphaBetaChain = alphaBetaChainBuilder.build();

        alphaBetaFirstChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaFirstChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaFirstChainBuilder.withNext(alphaBetaChain);
        alphaBetaFirstChainBuilder.withQuiescence(quiescenceChain);

        return alphaBetaFirstChainBuilder.build();
    }


}
