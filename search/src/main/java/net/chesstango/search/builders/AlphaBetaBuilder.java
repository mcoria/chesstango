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
import net.chesstango.search.smart.alphabeta.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchContext;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveDebug;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.features.pv.listeners.SetPVStatistics;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.features.statistics.evaluation.GameEvaluatorStatisticsWrapper;
import net.chesstango.search.smart.features.statistics.game.SearchMoveGameWrapper;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.transposition.listeners.SetTranspositionTables;
import net.chesstango.search.smart.features.transposition.listeners.SetTranspositionTablesDebug;
import net.chesstango.search.smart.features.zobrist.listeners.SetZobristMemory;

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
    private GameEvaluatorCache gameEvaluatorCache;
    private GameEvaluatorStatisticsWrapper gameEvaluatorStatisticsWrapper;
    private SetTranspositionTables setTranspositionTables;
    private SetTranspositionTablesDebug setTranspositionTablesDebug;
    private SetKillerMoveDebug setKillerMoveDebug;
    private SetNodeStatistics setNodeStatistics;
    private SetPVStatistics setPVStatistics;
    private SetTrianglePV setTrianglePV;
    private SetZobristMemory setZobristMemory;
    private SetDebugOutput setDebugOutput;
    private SetSearchTracker setSearchTracker;
    private SetKillerMoveTables setKillerMoveTables;
    private DebugNodeTrap debugNodeTrap;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
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
    private boolean showNodeTranspositionAccess;
    private boolean showSorterOperations;
    private boolean withAspirationWindows;
    private boolean withKillerMoveSorter;

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
        terminalChainBuilder.withTranspositionTable();


        quiescenceChainBuilder.withTranspositionTable();
        checkResolverChainBuilder.withTranspositionTable();
        quiescenceTerminalChainBuilder.withTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        alphaBetaInteriorChainBuilder.withTranspositionMoveSorter();
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

    public AlphaBetaBuilder withKillerMoveSorter() {
        alphaBetaInteriorChainBuilder.withKillerMoveSorter();
        withKillerMoveSorter = true;
        return this;
    }

    public AlphaBetaBuilder withRecaptureSorter() {
        alphaBetaInteriorChainBuilder.withRecaptureSorter();
        quiescenceChainBuilder.withRecaptureSorter();
        return this;
    }

    public AlphaBetaBuilder withMvvLvaSorter() {
        alphaBetaInteriorChainBuilder.withMvvLvaSorter();
        quiescenceChainBuilder.withMvvLvaSorter();
        return this;
    }

    public AlphaBetaBuilder withDebugSearchTree(DebugNodeTrap debugNodeTrap, boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showSorterOperations) {
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
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showSorterOperations = showSorterOperations;
        return this;
    }

    @Override
    public SearchMove build() {
        if (!withTranspositionTable) {
            withTriangularPV();
        }

        if (withTriangularPV && withTranspositionTable) {
            throw new RuntimeException("TranspositionTable and TriangularPV are incompatibles features");
        }

        buildObjects();

        setupListenerMediatorBeforeChain();

        alphaBetaFacade.setAlphaBetaFilter(createChain());

        setupListenerMediatorAfterChain();

        SearchMove searchMove;

        if (withIterativeDeepening) {
            searchMove = new IterativeDeepening(alphaBetaFacade, smartListenerMediator);
        } else {
            searchMove = new NoIterativeDeepening(alphaBetaFacade, smartListenerMediator);
        }

        if (withStatistics) {
            SearchMoveGameWrapper searchMoveGameWrapper = new SearchMoveGameWrapper(searchMove);
            smartListenerMediator.add(searchMoveGameWrapper);

            searchMove = searchMoveGameWrapper;
        }

        if (withPrintChain) {
            new ChainPrinter().printChain(searchMove);
        }

        return searchMove;
    }

    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new GameEvaluatorCache(gameEvaluator);

            gameEvaluator = gameEvaluatorCache;
        }

        if (withStatistics) {
            gameEvaluatorStatisticsWrapper = new GameEvaluatorStatisticsWrapper()
                    .setImp(gameEvaluator)
                    .setGameEvaluatorCache(gameEvaluatorCache)
                    .setTrackEvaluations(withTrackEvaluations);

            gameEvaluator = gameEvaluatorStatisticsWrapper;
        }

        if (withTranspositionTable) {
            if (withDebugSearchTree) {
                setTranspositionTablesDebug = new SetTranspositionTablesDebug();
            } else {
                setTranspositionTables = new SetTranspositionTables();
            }
            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }
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
            setSearchTracker = new SetSearchTracker(debugNodeTrap);
            setDebugOutput = new SetDebugOutput(withAspirationWindows, debugNodeTrap, showOnlyPV, showNodeTranspositionAccess, showSorterOperations);
        }

        if (withKillerMoveSorter) {
            if (withDebugSearchTree) {
                setKillerMoveDebug = new SetKillerMoveDebug();
            } else {
                setKillerMoveTables = new SetKillerMoveTables();
            }
        }

    }


    private void setupListenerMediatorBeforeChain() {
        smartListenerMediator.add(setSearchContext);

        if (setSearchTracker != null) {
            smartListenerMediator.add(setSearchTracker);
        }

        if (setTranspositionTables != null) {
            smartListenerMediator.add(setTranspositionTables);
        } else if (setTranspositionTablesDebug != null) {
            smartListenerMediator.add(setTranspositionTablesDebug);
            smartListenerMediator.add(setTranspositionTablesDebug.getMaxMap());
            smartListenerMediator.add(setTranspositionTablesDebug.getMinMap());
            smartListenerMediator.add(setTranspositionTablesDebug.getQMaxMap());
            smartListenerMediator.add(setTranspositionTablesDebug.getQMinMap());
        }

        if (setZobristMemory != null) {
            smartListenerMediator.add(setZobristMemory);
        }

        if (setTrianglePV != null) {
            smartListenerMediator.add(setTrianglePV);
        }

        if (setNodeStatistics != null) {
            smartListenerMediator.add(setNodeStatistics);
        }

        if (gameEvaluatorStatisticsWrapper != null) {
            smartListenerMediator.add(gameEvaluatorStatisticsWrapper);
        }

        if (setKillerMoveTables != null) {
            smartListenerMediator.add(setKillerMoveTables);
        } else if (setKillerMoveDebug != null) {
            smartListenerMediator.add(setKillerMoveDebug);
            smartListenerMediator.add(setKillerMoveDebug.getKillerMovesDebug());
        }

        smartListenerMediator.add(setGameEvaluator);

        smartListenerMediator.add(alphaBetaFacade);

        smartListenerMediator.add(alphaBetaFlowControl);

        smartListenerMediator.add(extensionFlowControl);
    }

    private void setupListenerMediatorAfterChain() {
        if (setPVStatistics != null) {
            smartListenerMediator.add(setPVStatistics);
        }
        if (setDebugOutput != null) {
            smartListenerMediator.add(setDebugOutput);
        }
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
        alphaBetaInteriorChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
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
        alphaBetaRootChainBuilder.withGameEvaluator(gameEvaluator);

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
            quiescenceChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
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

