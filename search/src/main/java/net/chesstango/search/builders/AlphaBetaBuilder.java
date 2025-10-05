package net.chesstango.search.builders;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.builders.alphabeta.*;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.alphabeta.listeners.SetSearchContext;
import net.chesstango.search.smart.features.debug.DebugNodeTrap;
import net.chesstango.search.smart.features.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.features.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.features.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.features.pv.listeners.SetPVStatistics;
import net.chesstango.search.smart.features.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;
import net.chesstango.search.smart.features.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.features.transposition.listeners.SetTranspositionTables;
import net.chesstango.search.smart.features.transposition.listeners.SetTranspositionTablesDebug;
import net.chesstango.search.smart.features.zobrist.listeners.SetZobristMemory;
import net.chesstango.search.visitors.SetSearchListenerMediator;

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
    private final SearchListenerMediator searchListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private final ExtensionFlowControl extensionFlowControl;
    private Evaluator evaluator;
    private EvaluatorCache gameEvaluatorCache;
    private EvaluatorStatisticsWrapper gameEvaluatorStatisticsWrapper;
    private SetTranspositionTables setTranspositionTables;
    private SetTranspositionTablesDebug setTranspositionTablesDebug;
    private SetKillerMoveTablesDebug setKillerMoveTablesDebug;
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
        searchListenerMediator = new SearchListenerMediator();
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
    public AlphaBetaBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
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
    public Search build() {
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

        searchListenerMediator.accept(new SetSearchListenerMediator(searchListenerMediator));

        Search search;
        if (withIterativeDeepening) {
            search = new IterativeDeepening(alphaBetaFacade, searchListenerMediator);
        } else {
            search = new NoIterativeDeepening(alphaBetaFacade, searchListenerMediator);
        }

        return search;
    }

    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new EvaluatorCache(evaluator);

            evaluator = gameEvaluatorCache;
        }

        if (withStatistics) {
            gameEvaluatorStatisticsWrapper = new EvaluatorStatisticsWrapper()
                    .setImp(evaluator)
                    .setGameEvaluatorCache(gameEvaluatorCache)
                    .setTrackEvaluations(withTrackEvaluations);

            evaluator = gameEvaluatorStatisticsWrapper;
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
            setTrianglePV.setEvaluator(evaluator);
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
                setKillerMoveTablesDebug = new SetKillerMoveTablesDebug();
            } else {
                setKillerMoveTables = new SetKillerMoveTables();
            }
        }

    }


    private void setupListenerMediatorBeforeChain() {
        searchListenerMediator.add(setGameEvaluator);

        searchListenerMediator.add(alphaBetaFacade);

        searchListenerMediator.add(setSearchContext);

        if (setSearchTracker != null) {
            searchListenerMediator.add(setSearchTracker);
        }

        if (setTranspositionTables != null) {
            searchListenerMediator.add(setTranspositionTables);
        } else if (setTranspositionTablesDebug != null) {
            searchListenerMediator.add(setTranspositionTablesDebug);
            searchListenerMediator.addAcceptor(setTranspositionTablesDebug.getMaxMap());
            searchListenerMediator.addAcceptor(setTranspositionTablesDebug.getMinMap());
            searchListenerMediator.addAcceptor(setTranspositionTablesDebug.getQMaxMap());
            searchListenerMediator.addAcceptor(setTranspositionTablesDebug.getQMinMap());
        }

        if (setZobristMemory != null) {
            searchListenerMediator.add(setZobristMemory);
        }

        if (setTrianglePV != null) {
            searchListenerMediator.add(setTrianglePV);
        }

        if (setNodeStatistics != null) {
            searchListenerMediator.add(setNodeStatistics);
        }

        if (gameEvaluatorStatisticsWrapper != null) {
            searchListenerMediator.add(gameEvaluatorStatisticsWrapper);
        }

        if (setKillerMoveTables != null) {
            searchListenerMediator.add(setKillerMoveTables);
        } else if (setKillerMoveTablesDebug != null) {
            searchListenerMediator.add(setKillerMoveTablesDebug);
            searchListenerMediator.addAcceptor(setKillerMoveTablesDebug.getKillerMovesDebug());
        }

        searchListenerMediator.add(alphaBetaFlowControl);

        searchListenerMediator.add(extensionFlowControl);
    }

    private void setupListenerMediatorAfterChain() {
        if (setPVStatistics != null) {
            searchListenerMediator.add(setPVStatistics);
        }
        if (setDebugOutput != null) {
            searchListenerMediator.add(setDebugOutput);
        }
    }


    private AlphaBetaFilter createChain() {
        setGameEvaluator.setEvaluator(evaluator);

        terminalChainBuilder.withSmartListenerMediator(searchListenerMediator);
        terminalChainBuilder.withGameEvaluator(evaluator);
        AlphaBetaFilter terminalChain = terminalChainBuilder.build();

        leafChainBuilder.withGameEvaluator(evaluator);
        leafChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter leafChain = leafChainBuilder.build();


        AlphaBetaFilter extensionChain = createExtensionChain();
        alphaBetaHorizonChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaHorizonChainBuilder.withGameEvaluator(evaluator);
        alphaBetaHorizonChainBuilder.withExtension(extensionChain);
        AlphaBetaFilter horizonChain = alphaBetaHorizonChainBuilder.build();

        alphaBetaInteriorChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        alphaBetaInteriorChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        loopChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter loopChain = loopChainBuilder.build();

        alphaBetaFlowControl.setHorizonNode(horizonChain);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);
        alphaBetaFlowControl.setLoopNode(loopChain);
        alphaBetaFlowControl.setLeafNode(leafChain);

        alphaBetaRootChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaRootChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        alphaBetaRootChainBuilder.withGameEvaluator(evaluator);

        return alphaBetaRootChainBuilder.build();
    }

    private AlphaBetaFilter createExtensionChain() {
        AlphaBetaFilter quiescenceChain;
        AlphaBetaFilter quiescenceLeaf;
        AlphaBetaFilter checkResolverChain;
        AlphaBetaFilter loopChain;

        if (withQuiescence) {
            quiescenceChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceChainBuilder.withGameEvaluator(evaluator);
            quiescenceChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
            quiescenceChainBuilder.withExtensionFlowControl(extensionFlowControl);
            quiescenceChain = quiescenceChainBuilder.build();

            quiescenceLeafChainBuilder.withGameEvaluator(evaluator);
            quiescenceLeafChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceLeaf = quiescenceLeafChainBuilder.build();

            quiescenceTerminalChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceTerminalChainBuilder.withGameEvaluator(evaluator);
            AlphaBetaFilter quiescenceTerminalChain = quiescenceTerminalChainBuilder.build();

            if (withExtensionCheckResolver) {
                checkResolverChainBuilder.withSmartListenerMediator(searchListenerMediator);
                checkResolverChainBuilder.withGameEvaluator(evaluator);
                checkResolverChainBuilder.withExtensionFlowControl(extensionFlowControl);
                checkResolverChain = checkResolverChainBuilder.build();

                quiescenceLoopChainBuilder.withSmartListenerMediator(searchListenerMediator);
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
            quiescenceNullChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceNullChainBuilder.withGameEvaluator(evaluator);
            return quiescenceNullChainBuilder.build();
        }
    }


    public static AlphaBetaBuilder createDefaultBuilderInstance() {
        return new AlphaBetaBuilder()
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()
                .withIterativeDeepening()

                .withStopProcessingCatch();
    }
}

