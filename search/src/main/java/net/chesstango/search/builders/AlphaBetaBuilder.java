package net.chesstango.search.builders;


import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.GameEvaluatorCache;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveGameWrapper;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.SetDebugSearch;
import net.chesstango.search.smart.alphabeta.debug.SetDebugTranspositionTables;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.EvaluatorStatistics;
import net.chesstango.search.smart.alphabeta.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.listeners.*;
import net.chesstango.search.smart.statistics.GameStatistics;
import net.chesstango.search.smart.statistics.GameStatisticsByCycleListener;

/**
 * @author Mauricio Corias
 */
public class AlphaBetaBuilder implements SearchBuilder {
    private final SetSearchContext setSearchContext;
    private final AlphaBetaRootChainBuilder alphaBetaRootChainBuilder;
    private final AlphaBetaInteriorChainBuilder alphaBetaInteriorChainBuilder;
    private final TerminalChainBuilder terminalChainBuilder;
    private final TerminalChainBuilder quiescenceTerminalChainBuilder;
    private final AlphaBetaHorizonChainBuilder alphaBetaHorizonChainBuilder;
    private final LoopChainBuilder loopChainBuilder;
    private final LoopChainBuilder quiescenceLoopChainBuilder;
    private final QuiescenceChainBuilder quiescenceChainBuilder;
    private final LeafChainBuilder quiescenceLeafChainBuilder;
    private final LeafChainBuilder leafChainBuilder;
    private final QuiescenceNullChainBuilder quiescenceNullChainBuilder;
    private final CheckResolverChainBuilder checkResolverChainBuilder;
    private final SetGameEvaluator setGameEvaluator;
    private final AlphaBetaFacade alphaBetaFacade;
    private final SmartListenerMediator smartListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private final ExtensionFlowControl extensionFlowControl;
    private GameEvaluator gameEvaluator;
    private SetTranspositionTables setTranspositionTables;
    private SetDebugTranspositionTables setDebugTranspositionTables;
    private SetTranspositionPV setTranspositionPV;
    private SetNodeStatistics setNodeStatistics;
    private SetPVStatistics setPVStatistics;
    private GameStatisticsByCycleListener gameStatisticsListener;
    private SetTrianglePV setTrianglePV;
    private SetZobristMemory setZobristMemory;
    private SetDebugSearch setDebugSearch;
    private DebugNodeTrap debugNodeTrap;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withQTranspositionTable;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withTriangularPV;
    private boolean withZobristTracker;
    private boolean withQuiescence;
    private boolean withExtensionCheckResolver;
    private boolean withPrintChain;
    private boolean withDebugSearchTree;
    private boolean showOnlyPV;
    private boolean showTranspositionAccess;
    private boolean withAspirationWindows;

    public AlphaBetaBuilder() {
        alphaBetaRootChainBuilder = new AlphaBetaRootChainBuilder();
        alphaBetaInteriorChainBuilder = new AlphaBetaInteriorChainBuilder();
        alphaBetaHorizonChainBuilder = new AlphaBetaHorizonChainBuilder();

        quiescenceChainBuilder = new QuiescenceChainBuilder();
        quiescenceNullChainBuilder = new QuiescenceNullChainBuilder();

        checkResolverChainBuilder = new CheckResolverChainBuilder();

        alphaBetaFacade = new AlphaBetaFacade();
        setGameEvaluator = new SetGameEvaluator();
        smartListenerMediator = new SmartListenerMediator();
        alphaBetaFlowControl = new AlphaBetaFlowControl();
        extensionFlowControl = new ExtensionFlowControl();

        setSearchContext = new SetSearchContext();

        terminalChainBuilder = new TerminalChainBuilder();
        quiescenceTerminalChainBuilder = new TerminalChainBuilder();

        leafChainBuilder = new LeafChainBuilder();
        quiescenceLeafChainBuilder = new LeafChainBuilder();

        loopChainBuilder = new LoopChainBuilder();
        quiescenceLoopChainBuilder = new LoopChainBuilder();
    }

    public AlphaBetaBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }


    @Override
    public AlphaBetaBuilder withGameEvaluator(GameEvaluator evaluator) {
        gameEvaluator = evaluator;
        return this;
    }

    @Override
    public AlphaBetaBuilder withGameEvaluatorCache() {
        withGameEvaluatorCache = true;
        return this;
    }

    public AlphaBetaBuilder withQuiescence() {
        withQuiescence = true;
        return this;
    }

    public AlphaBetaBuilder withExtensionCheckResolver() {
        withExtensionCheckResolver = true;
        return this;
    }

    public AlphaBetaBuilder withStatistics() {
        withStatistics = true;
        alphaBetaRootChainBuilder.withStatistics();
        alphaBetaInteriorChainBuilder.withStatistics();
        quiescenceChainBuilder.withStatistics();
        checkResolverChainBuilder.withStatistics();
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        withTranspositionTable = true;
        alphaBetaRootChainBuilder.withTranspositionTable();
        alphaBetaInteriorChainBuilder.withTranspositionTable();
        alphaBetaHorizonChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        alphaBetaInteriorChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionTable() {
        if (!withQuiescence) {
            throw new RuntimeException("You must enable Quiescence first");
        }
        withQTranspositionTable = true;
        quiescenceChainBuilder.withTranspositionTable();
        checkResolverChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionMoveSorter() {
        if (!withQTranspositionTable) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        quiescenceChainBuilder.withTranspositionMoveSorter();
        //checkResolverChainBuilder.withTranspositionMoveSorter();
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

    public AlphaBetaBuilder withComposedMoveSorter() {
        alphaBetaInteriorChainBuilder.withComposedMoveSorter();
        quiescenceChainBuilder.withComposedMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withZobristTracker() {
        withZobristTracker = true;

        alphaBetaRootChainBuilder.withZobristTracker();
        alphaBetaInteriorChainBuilder.withZobristTracker();
        terminalChainBuilder.withZobristTracker();
        alphaBetaHorizonChainBuilder.withZobristTracker();
        loopChainBuilder.withZobristTracker();

        quiescenceLoopChainBuilder.withZobristTracker();
        quiescenceChainBuilder.withZobristTracker();
        quiescenceLeafChainBuilder.withZobristTracker();
        checkResolverChainBuilder.withZobristTracker();
        quiescenceTerminalChainBuilder.withZobristTracker();
        return this;
    }

    public AlphaBetaBuilder withAspirationWindows() {
        alphaBetaRootChainBuilder.withAspirationWindows();
        withAspirationWindows = true;
        return this;
    }

    public AlphaBetaBuilder withTriangularPV() {
        withTriangularPV = true;
        alphaBetaRootChainBuilder.withTriangularPV();
        alphaBetaInteriorChainBuilder.withTriangularPV();
        quiescenceChainBuilder.withTriangularPV();
        checkResolverChainBuilder.withTriangularPV();
        return this;
    }

    public AlphaBetaBuilder withPrintChain() {
        withPrintChain = true;
        return this;
    }

    public AlphaBetaBuilder withDebugSearchTree(DebugNodeTrap debugNodeTrap, boolean showOnlyPV, boolean showTranspositionAccess) {
        alphaBetaRootChainBuilder.withDebugSearchTree();
        alphaBetaInteriorChainBuilder.withDebugSearchTree();
        alphaBetaHorizonChainBuilder.withDebugSearchTree();
        terminalChainBuilder.withDebugSearchTree();
        loopChainBuilder.withDebugSearchTree();
        leafChainBuilder.withDebugSearchTree();

        quiescenceChainBuilder.withDebugSearchTree();
        quiescenceLeafChainBuilder.withDebugSearchTree();
        checkResolverChainBuilder.withDebugSearchTree();
        quiescenceTerminalChainBuilder.withDebugSearchTree();
        quiescenceLoopChainBuilder.withZobristTracker();

        this.withDebugSearchTree = true;
        this.debugNodeTrap = debugNodeTrap;
        this.showOnlyPV = showOnlyPV;
        this.showTranspositionAccess = showTranspositionAccess;
        return this;
    }

    @Override
    public SearchMove build() {
        if (!withTranspositionTable && !withQTranspositionTable) {
            withTriangularPV();
        }

        if (withTriangularPV && (withTranspositionTable || withQTranspositionTable)) {
            throw new RuntimeException("TranspositionTable and TriangularPV are incompatibles features");
        }

        buildObjects();

        setupListenerMediator();

        alphaBetaFacade.setAlphaBetaFilter(createChain());

        SearchMove searchMove;

        if (withIterativeDeepening) {
            searchMove = new IterativeDeepening(alphaBetaFacade, smartListenerMediator);
        } else {
            searchMove = new NoIterativeDeepening(alphaBetaFacade, smartListenerMediator);
        }

        if (withStatistics) {
            searchMove = new SearchMoveGameWrapper(searchMove, GameStatistics::new);
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
            if (withDebugSearchTree) {
                setDebugTranspositionTables = new SetDebugTranspositionTables();
            } else {
                setTranspositionTables = new SetTranspositionTables();
            }
            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }
            setTranspositionPV = new SetTranspositionPV();
            setTranspositionPV.setGameEvaluator(gameEvaluator);
        }

        if (withTriangularPV) {
            setTrianglePV = new SetTrianglePV();
            setTrianglePV.setGameEvaluator(gameEvaluator);
        }

        if (withStatistics) {
            setNodeStatistics = new SetNodeStatistics();
            setPVStatistics = new SetPVStatistics();
        }

        if (withZobristTracker) {
            setZobristMemory = new SetZobristMemory();
        }

        if (withDebugSearchTree) {
            setDebugSearch = new SetDebugSearch(withAspirationWindows, debugNodeTrap, showOnlyPV, showTranspositionAccess);
        }

    }

    private void setupListenerMediator() {
        smartListenerMediator.add(setSearchContext);

        if (withTranspositionTable) {
            if (withDebugSearchTree) {
                smartListenerMediator.add(setDebugTranspositionTables);
            } else {
                smartListenerMediator.add(setTranspositionTables);
            }
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
            smartListenerMediator.add(setPVStatistics);
            smartListenerMediator.add(gameStatisticsListener);
        }

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            smartListenerMediator.add(evaluatorStatistics);
        }

        if (setDebugSearch != null) {
            smartListenerMediator.add(setDebugSearch);

            if (setDebugTranspositionTables != null) {
                smartListenerMediator.add(setDebugTranspositionTables.getMaxMap());
                smartListenerMediator.add(setDebugTranspositionTables.getMinMap());
                smartListenerMediator.add(setDebugTranspositionTables.getQMaxMap());
                smartListenerMediator.add(setDebugTranspositionTables.getQMinMap());
            }
        }

        smartListenerMediator.add(setGameEvaluator);

        smartListenerMediator.add(alphaBetaFacade);

        smartListenerMediator.add(alphaBetaFlowControl);

        smartListenerMediator.add(extensionFlowControl);
    }


    private AlphaBetaFilter createChain() {
        setGameEvaluator.setGameEvaluator(gameEvaluator);

        terminalChainBuilder.withSmartListenerMediator(smartListenerMediator);
        terminalChainBuilder.withGameEvaluator(gameEvaluator);
        AlphaBetaFilter terminalChain = terminalChainBuilder.build();

        leafChainBuilder.withGameEvaluator(gameEvaluator);
        leafChainBuilder.withSmartListenerMediator(smartListenerMediator);
        AlphaBetaFilter leafChain = leafChainBuilder.build();


        AlphaBetaFilter extensionChain = createExtensionChain();
        alphaBetaHorizonChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaHorizonChainBuilder.withGameEvaluator(gameEvaluator);
        alphaBetaHorizonChainBuilder.withExtension(extensionChain);
        AlphaBetaFilter horizonChain = alphaBetaHorizonChainBuilder.build();

        alphaBetaInteriorChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        loopChainBuilder.withSmartListenerMediator(smartListenerMediator);
        AlphaBetaFilter loopChain = loopChainBuilder.build();

        alphaBetaFlowControl.setHorizonNode(horizonChain);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);
        alphaBetaFlowControl.setLoopNode(loopChain);
        alphaBetaFlowControl.setLeafNode(leafChain);

        alphaBetaRootChainBuilder.withSmartListenerMediator(smartListenerMediator);
        alphaBetaRootChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);

        return alphaBetaRootChainBuilder.build();
    }

    private AlphaBetaFilter createExtensionChain() {
        AlphaBetaFilter quiescenceChain;
        AlphaBetaFilter quiescenceLeaf;
        AlphaBetaFilter checkResolverChain;
        AlphaBetaFilter loopChain;

        if (withQuiescence) {
            quiescenceChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceChainBuilder.withGameEvaluator(gameEvaluator);
            quiescenceChainBuilder.withExtensionFlowControl(extensionFlowControl);
            quiescenceChain = quiescenceChainBuilder.build();

            quiescenceLeafChainBuilder.withGameEvaluator(gameEvaluator);
            quiescenceLeafChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceLeaf = quiescenceLeafChainBuilder.build();

            quiescenceTerminalChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceTerminalChainBuilder.withGameEvaluator(gameEvaluator);
            AlphaBetaFilter quiescenceTerminalChain = quiescenceTerminalChainBuilder.build();

            if (withExtensionCheckResolver) {
                checkResolverChainBuilder.withSmartListenerMediator(smartListenerMediator);
                checkResolverChainBuilder.withGameEvaluator(gameEvaluator);
                checkResolverChainBuilder.withExtensionFlowControl(extensionFlowControl);
                checkResolverChain = checkResolverChainBuilder.build();

                quiescenceLoopChainBuilder.withSmartListenerMediator(smartListenerMediator);
                loopChain = quiescenceLoopChainBuilder.build();
            } else {
                checkResolverChain = null;
                loopChain = null;
            }

            extensionFlowControl.setTerminalNode(quiescenceTerminalChain);
            extensionFlowControl.setQuiescenceNode(quiescenceChain);
            extensionFlowControl.setLeafNode(quiescenceLeaf);
            extensionFlowControl.setCheckResolverNode(checkResolverChain);
            extensionFlowControl.setLoopNode(loopChain);

            return extensionFlowControl;
        } else {
            quiescenceNullChainBuilder.withSmartListenerMediator(smartListenerMediator);
            quiescenceNullChainBuilder.withGameEvaluator(gameEvaluator);
            return quiescenceNullChainBuilder.build();
        }
    }

}

