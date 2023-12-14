package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.EvaluatorStatistics;
import net.chesstango.search.smart.alphabeta.listeners.*;
import net.chesstango.search.smart.statistics.GameStatisticsByCycleListener;
import net.chesstango.search.smart.statistics.SearchMoveWrapper;

/**
 * @author Mauricio Corias
 */
public class AlphaBetaBuilder implements SearchBuilder {
    private final AlphaBetaRootChainBuilder alphaBetaRootChainBuilder;
    private final AlphaBetaInteriorChainBuilder alphaBetaInteriorChainBuilder;
    private final AlphaBetaTerminalChainBuilder alphaBetaTerminalChainBuilder;
    private final AlphaBetaHorizonChainBuilder alphaBetaHorizonChainBuilder;
    private final QuiescenceChainBuilder quiescenceChainBuilder;
    private final QuiescenceNullChainBuilder quiescenceNullChainBuilder;
    private final SetupGameEvaluator setupGameEvaluator;
    private final AlphaBetaFacade alphaBetaFacade;
    private final SmartListenerMediator smartListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private GameEvaluator gameEvaluator;
    private SetTranspositionTables setTranspositionTables;
    private SetTranspositionPV setTranspositionPV;
    private SetNodeStatistics setNodeStatistics;
    private GameStatisticsByCycleListener gameStatisticsListener;
    private SetTrianglePV setTrianglePV;
    private SetContext setContext;
    private SetZobristMemory setZobristMemory;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withTriangularPV;
    private boolean withZobristTracker;
    private boolean withQuiescence;
    private boolean withPrintChain;


    public AlphaBetaBuilder() {
        alphaBetaRootChainBuilder = new AlphaBetaRootChainBuilder();
        alphaBetaInteriorChainBuilder = new AlphaBetaInteriorChainBuilder();
        alphaBetaTerminalChainBuilder = new AlphaBetaTerminalChainBuilder();
        alphaBetaHorizonChainBuilder = new AlphaBetaHorizonChainBuilder();

        quiescenceChainBuilder = new QuiescenceChainBuilder();
        quiescenceNullChainBuilder = new QuiescenceNullChainBuilder();

        alphaBetaFacade = new AlphaBetaFacade();
        setupGameEvaluator = new SetupGameEvaluator();
        smartListenerMediator = new SmartListenerMediator();
        alphaBetaFlowControl = new AlphaBetaFlowControl();
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
        this.withQuiescence = true;
        return this;
    }

    public AlphaBetaBuilder withStatistics() {
        this.withStatistics = true;
        alphaBetaRootChainBuilder.withStatistics();
        alphaBetaInteriorChainBuilder.withStatistics();

        quiescenceChainBuilder.withStatistics();
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        withTranspositionTable = true;
        alphaBetaRootChainBuilder.withTranspositionTable();
        alphaBetaInteriorChainBuilder.withTranspositionTable();
        alphaBetaTerminalChainBuilder.withTranspositionTable();
        alphaBetaHorizonChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        alphaBetaInteriorChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionTable() {
        quiescenceChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionMoveSorter() {
        quiescenceChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withStopProcessingCatch() {
        alphaBetaRootChainBuilder.withStopProcessingCatch();
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
        withZobristTracker = true;

        alphaBetaRootChainBuilder.withZobristTracker();
        alphaBetaInteriorChainBuilder.withZobristTracker();
        alphaBetaTerminalChainBuilder.withZobristTracker();
        alphaBetaHorizonChainBuilder.withZobristTracker();

        quiescenceChainBuilder.withZobristTracker();
        return this;
    }

    public AlphaBetaBuilder withAspirationWindows() {
        alphaBetaRootChainBuilder.withAspirationWindows();
        return this;
    }

    public AlphaBetaBuilder withTriangularPV() {
        withTriangularPV = true;
        alphaBetaRootChainBuilder.withTriangularPV();
        alphaBetaInteriorChainBuilder.withTriangularPV();
        return this;
    }

    public AlphaBetaBuilder withPrintChain() {
        this.withPrintChain = true;
        return this;
    }

    @Override
    public SearchMove build() {
        buildObjects();

        setupListenerMediator();

        alphaBetaFacade.setAlphaBetaFilter(createChain());

        SearchMove searchMove;

        if (withIterativeDeepening) {
            IterativeDeepening iterativeDeepening = new IterativeDeepening(alphaBetaFacade, smartListenerMediator);

            searchMove = iterativeDeepening;
        } else {
            NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(alphaBetaFacade, smartListenerMediator);

            searchMove = noIterativeDeepening;
        }

        if (withStatistics) {
            searchMove = new SearchMoveWrapper(searchMove);
        }

        if (withPrintChain) {
            new ChainPrinter().printChain(searchMove);
        }

        return searchMove;
    }

    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluator = new GameEvaluatorCache(gameEvaluator);
        }

        if (withStatistics) {
            gameEvaluator = new EvaluatorStatistics(gameEvaluator).setTrackEvaluations(withTrackEvaluations);
            gameStatisticsListener = new GameStatisticsByCycleListener();
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

        if (withIterativeDeepening) {
            setContext = new SetContext();
        }

        if (withZobristTracker) {
            setZobristMemory = new SetZobristMemory();
        }
    }


    private void setupListenerMediator() {
        if (setContext != null) {
            smartListenerMediator.add(setContext);
        }

        if (setTranspositionTables != null) {
            smartListenerMediator.add(setTranspositionTables);
        }

        if (setZobristMemory != null) {
            smartListenerMediator.add(setZobristMemory);
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

        smartListenerMediator.add(setupGameEvaluator);

        smartListenerMediator.add(alphaBetaFacade);

        smartListenerMediator.add(alphaBetaFlowControl);
    }


    private AlphaBetaFilter createChain() {
        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        AlphaBetaFilter quiescenceChain;
        if (withQuiescence) {
            quiescenceChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceChainBuilder.withGameEvaluator(gameEvaluator);
            quiescenceChain = quiescenceChainBuilder.build();
        } else {
            quiescenceNullChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceNullChainBuilder.withGameEvaluator(gameEvaluator);
            quiescenceChain = quiescenceNullChainBuilder.build();
        }

        alphaBetaTerminalChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaTerminalChainBuilder.withGameEvaluator(gameEvaluator);
        AlphaBetaFilter terminalChain = alphaBetaTerminalChainBuilder.build();


        alphaBetaHorizonChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaHorizonChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaHorizonChainBuilder.withQuiescence(quiescenceChain);
        AlphaBetaFilter horizonChain = alphaBetaHorizonChainBuilder.build();


        alphaBetaInteriorChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        alphaBetaFlowControl.setHorizonNode(horizonChain);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);

        alphaBetaRootChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaRootChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);

        return alphaBetaRootChainBuilder.build();
    }


}

