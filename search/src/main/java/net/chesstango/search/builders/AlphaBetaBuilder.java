package net.chesstango.search.builders;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.builders.alphabeta.*;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.filters.ExtensionFlowControl;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchLast;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBaseNull;
import net.chesstango.search.smart.alphabeta.egtb.visitors.SetEndGameTableBaseVisitor;
import net.chesstango.search.smart.alphabeta.evaluator.visitors.SetEvaluatorVisitor;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.alphabeta.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.alphabeta.statistics.node.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.transposition.listeners.ResetTranspositionTables;
import net.chesstango.search.smart.alphabeta.transposition.visitors.SetTTableVisitor;
import net.chesstango.search.smart.alphabeta.zobrist.listeners.SetZobristMemory;
import net.chesstango.search.visitors.SetSearchListenerMediatorVisitor;

/**
 * @author Mauricio Corias
 */
public class AlphaBetaBuilder implements SearchBuilder<AlphaBetaBuilder> {
    private final SetSearchTimers setSearchTimers;
    private final SetSearchLast setSearchLast;
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
    private final EgtbChainBuilder egtbChainBuilder;
    private final EgtbChainBuilder quiescencEgtbChainBuilder;
    private final TranspositionTableBuilder transpositionTableBuilder;
    private final EvaluationBuilder evaluationBuilder;

    private final SetGameToEvaluator setGameToEvaluator;
    private final AlphaBetaFacade alphaBetaFacade;
    private final SearchListenerMediator searchListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private final ExtensionFlowControl extensionFlowControl;

    private Evaluator evaluator;
    private EvaluatorCache gameEvaluatorCache;

    private ResetTranspositionTables resetTranspositionTables;
    private SetNodeStatistics setNodeStatistics;
    private SetTrianglePV setTrianglePV;
    private SetZobristMemory setZobristMemory;
    private SetDebugOutput setDebugOutput;
    private SetSearchTracker setSearchTracker;
    private SetKillerMoveTables setKillerMoveTables;
    private SetKillerMoveTablesDebug setKillerMoveTablesDebug;
    private DebugNodeTrap debugNodeTrap;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withTranspositionTableReuse;
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

        transpositionTableBuilder = new TranspositionTableBuilder();

        evaluationBuilder = new EvaluationBuilder();

        alphaBetaFacade = new AlphaBetaFacade();
        setGameToEvaluator = new SetGameToEvaluator();
        searchListenerMediator = new SearchListenerMediator();
        alphaBetaFlowControl = new AlphaBetaFlowControl();
        extensionFlowControl = new ExtensionFlowControl();

        setSearchTimers = new SetSearchTimers();
        setSearchLast = new SetSearchLast();

        terminalChainBuilder = new TerminalChainBuilder();
        quiescenceTerminalChainBuilder = new TerminalChainBuilder();

        leafChainBuilder = new LeafChainBuilder();
        quiescenceLeafChainBuilder = new LeafChainBuilder();

        loopChainBuilder = new LoopChainBuilder();
        quiescenceLoopChainBuilder = new LoopChainBuilder();

        egtbChainBuilder = new EgtbChainBuilder();
        quiescencEgtbChainBuilder = new EgtbChainBuilder();
    }

    public AlphaBetaBuilder withIterativeDeepening() {
        withIterativeDeepening = true;
        return this;
    }


    @Override
    public AlphaBetaBuilder withGameEvaluator(Evaluator evaluator) {
        evaluationBuilder.withGameEvaluator(evaluator);
        return this;
    }

    public AlphaBetaBuilder withGameEvaluatorCache() {
        evaluationBuilder.withGameEvaluatorCache();
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
        transpositionTableBuilder.withStatistics();
        evaluationBuilder.withStatistics();
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
        evaluationBuilder.withTrackEvaluations();
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

    public AlphaBetaBuilder withDebugNodeTrap(DebugNodeTrap debugNodeTrap) {
        this.debugNodeTrap = debugNodeTrap;
        return this;
    }

    public AlphaBetaBuilder withDebugSearchTree(boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showSorterOperations) {
        alphaBetaRootChainBuilder.withDebugSearchTree();
        alphaBetaInteriorChainBuilder.withDebugSearchTree();
        alphaBetaHorizonChainBuilder.withDebugSearchTree();
        terminalChainBuilder.withDebugSearchTree();
        loopChainBuilder.withDebugSearchTree();
        leafChainBuilder.withDebugSearchTree();
        egtbChainBuilder.withDebugSearchTree();
        transpositionTableBuilder.withDebugSearchTree();

        quiescenceChainBuilder.withDebugSearchTree();
        quiescenceLeafChainBuilder.withDebugSearchTree();
        checkResolverChainBuilder.withDebugSearchTree();
        quiescenceTerminalChainBuilder.withDebugSearchTree();
        quiescenceLoopChainBuilder.withDebugSearchTree();
        quiescencEgtbChainBuilder.withDebugSearchTree();

        this.withDebugSearchTree = true;
        this.showOnlyPV = showOnlyPV;
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showSorterOperations = showSorterOperations;
        return this;
    }

    @Override
    public Search build() {
        if (withTriangularPV && withTranspositionTable) {
            throw new RuntimeException("TranspositionTable and TriangularPV are incompatibles features");
        }

        if (withTranspositionTable) {
            transpositionTableBuilder.withSmartListenerMediator(searchListenerMediator);
        } else {
            withTriangularPV();
        }

        buildObjects();

        setupListenerMediatorBeforeChain();

        alphaBetaFacade.setAlphaBetaFilter(createChain());

        setupListenerMediatorAfterChain();

        searchListenerMediator.accept(new SetSearchListenerMediatorVisitor(searchListenerMediator));
        searchListenerMediator.accept(new SetEndGameTableBaseVisitor(new EndGameTableBaseNull()));
        searchListenerMediator.accept(new SetEvaluatorVisitor(evaluator));

        if (withTranspositionTable) {
            transpositionTableBuilder.build();
            searchListenerMediator.accept(new SetTTableVisitor(
                    transpositionTableBuilder.getMaxMap(),
                    transpositionTableBuilder.getMinMap(),
                    transpositionTableBuilder.getQMaxMap(),
                    transpositionTableBuilder.getQMinMap()
            ));
        }

        Search search;
        if (withIterativeDeepening) {
            search = new IterativeDeepening(alphaBetaFacade, searchListenerMediator);
        } else {
            search = new NoIterativeDeepening(alphaBetaFacade, searchListenerMediator);
        }

        return search;
    }

    private void buildObjects() {
        evaluator = evaluationBuilder
                .withSmartListenerMediator(searchListenerMediator)
                .build();
        gameEvaluatorCache = evaluationBuilder.getGameEvaluatorCache(); // CAMBIAR ESTE DISENO

        if (withTranspositionTable) {
            resetTranspositionTables = new ResetTranspositionTables();
            if (withTranspositionTableReuse) {
                resetTranspositionTables.setReuseTranspositionTable(true);
            }
        }

        if (withTriangularPV) {
            setTrianglePV = new SetTrianglePV();
        }

        if (withStatistics) {
            setNodeStatistics = new SetNodeStatistics();
        }

        if (withZobristTracker) {
            setZobristMemory = new SetZobristMemory();
        }

        if (withDebugSearchTree) {
            setSearchTracker = new SetSearchTracker();
            setDebugOutput = new SetDebugOutput(withAspirationWindows, showOnlyPV, showNodeTranspositionAccess, showSorterOperations);
            if (debugNodeTrap != null) {
                setDebugOutput.setDebugNodeTrap(debugNodeTrap);
            }
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
        searchListenerMediator.addAcceptor(setGameToEvaluator);

        searchListenerMediator.add(alphaBetaFacade);

        searchListenerMediator.add(setSearchTimers);

        searchListenerMediator.add(setSearchLast);

        if (setSearchTracker != null) {
            searchListenerMediator.add(setSearchTracker);
        }

        if (resetTranspositionTables != null) {
            searchListenerMediator.add(resetTranspositionTables);
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
        if (debugNodeTrap instanceof SearchListener debugNodeTrapSearchListener) {
            searchListenerMediator.add(debugNodeTrapSearchListener);
        } else if (debugNodeTrap instanceof Acceptor debugNodeTrapAcceptor) {
            searchListenerMediator.addAcceptor(debugNodeTrapAcceptor);
        }
        if (setDebugOutput != null) {
            searchListenerMediator.add(setDebugOutput);
        }
    }


    private AlphaBetaFilter createChain() {
        terminalChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter terminalChain = terminalChainBuilder.build();

        leafChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter leafChain = leafChainBuilder.build();

        AlphaBetaFilter extensionChain = createExtensionChain();
        alphaBetaHorizonChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaHorizonChainBuilder.withExtension(extensionChain);
        AlphaBetaFilter horizonChain = alphaBetaHorizonChainBuilder.build();

        alphaBetaInteriorChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        alphaBetaInteriorChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        loopChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter loopChain = loopChainBuilder.build();

        egtbChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter egtbChain = egtbChainBuilder.build();

        alphaBetaFlowControl.setHorizonNode(horizonChain);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);
        alphaBetaFlowControl.setLoopNode(loopChain);
        alphaBetaFlowControl.setLeafNode(leafChain);
        alphaBetaFlowControl.setEgtbNode(egtbChain);

        alphaBetaRootChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaRootChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);

        return alphaBetaRootChainBuilder.build();
    }

    private AlphaBetaFilter createExtensionChain() {
        AlphaBetaFilter quiescenceChain;
        AlphaBetaFilter quiescenceLeaf;
        AlphaBetaFilter checkResolverChain;
        AlphaBetaFilter loopChain;

        if (withQuiescence) {
            quiescenceChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
            quiescenceChainBuilder.withExtensionFlowControl(extensionFlowControl);
            quiescenceChain = quiescenceChainBuilder.build();

            quiescenceLeafChainBuilder.withSmartListenerMediator(searchListenerMediator);
            quiescenceLeaf = quiescenceLeafChainBuilder.build();

            quiescenceTerminalChainBuilder.withSmartListenerMediator(searchListenerMediator);
            AlphaBetaFilter quiescenceTerminalChain = quiescenceTerminalChainBuilder.build();

            quiescencEgtbChainBuilder.withSmartListenerMediator(searchListenerMediator);
            AlphaBetaFilter egtbChain = quiescencEgtbChainBuilder.build();

            if (withExtensionCheckResolver) {
                checkResolverChainBuilder.withSmartListenerMediator(searchListenerMediator);
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
            extensionFlowControl.setEgtbNode(egtbChain);
            extensionFlowControl.setCheckResolverNode(checkResolverChain);
            extensionFlowControl.setLoopNode(loopChain);

            return extensionFlowControl;
        } else {
            quiescenceNullChainBuilder.withSmartListenerMediator(searchListenerMediator);
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

