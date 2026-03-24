package net.chesstango.search.builders;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.builders.alphabeta.*;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBaseNull;
import net.chesstango.search.smart.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.alphabeta.egtb.visitors.SetEndGameTableBaseVisitor;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.alphabeta.pv.listeners.SetTrianglePV;
import net.chesstango.search.smart.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.smart.alphabeta.statistics.game.GameCountersCollector;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.smart.alphabeta.statistics.node.visitors.LinkNodeCountersVisitor;
import net.chesstango.search.smart.alphabeta.zobrist.listeners.SetZobristMemory;
import net.chesstango.search.visitors.SetSearchListenerMediatorVisitor;

/**
 * @author Mauricio Corias
 */
public class AlphaBetaBuilder implements SearchBuilder<AlphaBetaBuilder> {
    private final SetSearchTimers setSearchTimers;
    private final AlphaBetaRootChainBuilder alphaBetaRootChainBuilder;
    private final AlphaBetaInteriorChainBuilder alphaBetaInteriorChainBuilder;
    private final TerminalChainBuilder terminalChainBuilder;

    private final LoopChainBuilder loopChainBuilder;
    private final QuiescenceChainBuilder quiescenceChainBuilder;
    private final LeafChainBuilder leafChainBuilder;
    private final CheckResolverChainBuilder checkResolverChainBuilder;
    private final TranspositionTableBuilder transpositionTableBuilder;
    private final EvaluationBuilder evaluationBuilder;
    private final EgtbChainBuilder egtbChainBuilder;

    private final SetGameToEndGameTableBase setGameToEndGameTableBase;

    private final AlphaBetaFacade alphaBetaFacade;
    private final SearchListenerMediator searchListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;

    private NodeCounters nodeCounters;
    private GameCountersCollector gameCounters;
    private DepthCollector depthCollector;
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

    private Search search;

    public AlphaBetaBuilder() {
        alphaBetaRootChainBuilder = new AlphaBetaRootChainBuilder();
        alphaBetaInteriorChainBuilder = new AlphaBetaInteriorChainBuilder();

        quiescenceChainBuilder = new QuiescenceChainBuilder();
        checkResolverChainBuilder = new CheckResolverChainBuilder();
        transpositionTableBuilder = new TranspositionTableBuilder();
        evaluationBuilder = new EvaluationBuilder();

        alphaBetaFacade = new AlphaBetaFacade();
        searchListenerMediator = new SearchListenerMediator();
        alphaBetaFlowControl = new AlphaBetaFlowControl();

        setSearchTimers = new SetSearchTimers();

        terminalChainBuilder = new TerminalChainBuilder();

        leafChainBuilder = new LeafChainBuilder();

        loopChainBuilder = new LoopChainBuilder();

        egtbChainBuilder = new EgtbChainBuilder();
        setGameToEndGameTableBase = new SetGameToEndGameTableBase();
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
        alphaBetaInteriorChainBuilder.withGameEvaluatorCache();
        quiescenceChainBuilder.withGameEvaluatorCache();
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
        terminalChainBuilder.withStatistics();
        leafChainBuilder.withStatistics();
        loopChainBuilder.withStatistics();
        egtbChainBuilder.withStatistics();
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        withTranspositionTable = true;
        alphaBetaRootChainBuilder.withTranspositionTable();
        alphaBetaInteriorChainBuilder.withTranspositionTable();

        quiescenceChainBuilder.withTranspositionTable();
        checkResolverChainBuilder.withTranspositionTable();
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

    public AlphaBetaBuilder withTrackEvaluations() {
        evaluationBuilder.withTrackEvaluations();
        return this;
    }

    public AlphaBetaBuilder withZobristTracker() {
        withZobristTracker = true;

        alphaBetaRootChainBuilder.withZobristTracker();
        alphaBetaInteriorChainBuilder.withZobristTracker();
        terminalChainBuilder.withZobristTracker();
        loopChainBuilder.withZobristTracker();

        quiescenceChainBuilder.withZobristTracker();
        checkResolverChainBuilder.withZobristTracker();
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
        terminalChainBuilder.withDebugSearchTree();
        loopChainBuilder.withDebugSearchTree();
        leafChainBuilder.withDebugSearchTree();
        egtbChainBuilder.withDebugSearchTree();
        transpositionTableBuilder.withDebugSearchTree();

        quiescenceChainBuilder.withDebugSearchTree();
        checkResolverChainBuilder.withDebugSearchTree();

        evaluationBuilder.withDebugSearchTree();

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

        if (!withTranspositionTable) {
            withTriangularPV();
        }

        buildObjects();

        setupListenerMediatorBeforeChain();

        alphaBetaFacade.setAlphaBetaFilter(createChain());

        setupListenerMediatorAfterChain();

        link();

        return search;
    }

    private void link() {
        searchListenerMediator.accept(new SetSearchListenerMediatorVisitor(searchListenerMediator));
        searchListenerMediator.accept(new SetEndGameTableBaseVisitor(new EndGameTableBaseNull()));

        if (withTranspositionTable) {
            transpositionTableBuilder.link();
        }

        if (withStatistics) {
            searchListenerMediator.accept(new LinkNodeCountersVisitor(nodeCounters));
        }

        evaluationBuilder.link();
    }

    private void buildObjects() {
        evaluationBuilder
                .withSmartListenerMediator(searchListenerMediator)
                .build();


        if (withIterativeDeepening) {
            search = new IterativeDeepening(alphaBetaFacade, searchListenerMediator);
        } else {
            search = new NoIterativeDeepening(alphaBetaFacade, searchListenerMediator);
        }

        if (withTranspositionTable) {
            transpositionTableBuilder.withSmartListenerMediator(searchListenerMediator);
            transpositionTableBuilder.build();
        }

        if (withTriangularPV) {
            setTrianglePV = new SetTrianglePV();
        }

        if (withStatistics) {
            nodeCounters = new NodeCounters();
            gameCounters = new GameCountersCollector();

            depthCollector = new DepthCollector();
            depthCollector.setRootMoveEvaluationCollection(alphaBetaRootChainBuilder.getMoveEvaluations());
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
        searchListenerMediator.add(setGameToEndGameTableBase);

        searchListenerMediator.add(alphaBetaFacade);

        searchListenerMediator.add(setSearchTimers);

        searchListenerMediator.add(alphaBetaFlowControl);

        if (setSearchTracker != null) {
            searchListenerMediator.add(setSearchTracker);
        }

        if (setZobristMemory != null) {
            searchListenerMediator.add(setZobristMemory);
        }

        if (setTrianglePV != null) {
            searchListenerMediator.add(setTrianglePV);
        }

        if (nodeCounters != null) {
            searchListenerMediator.add(nodeCounters);
        }

        if (gameCounters != null) {
            searchListenerMediator.add(gameCounters);
        }

        if (depthCollector != null) {
            searchListenerMediator.add(depthCollector);
        }

        if (setKillerMoveTables != null) {
            searchListenerMediator.add(setKillerMoveTables);
        }

        if (setKillerMoveTablesDebug != null) {
            searchListenerMediator.add(setKillerMoveTablesDebug);
            searchListenerMediator.add(setKillerMoveTablesDebug.getKillerMovesDebug());
        }
    }

    private void setupListenerMediatorAfterChain() {
        if (debugNodeTrap instanceof Acceptor debugNodeTrapAcceptor) {
            searchListenerMediator.add(debugNodeTrapAcceptor);
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

        alphaBetaInteriorChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        loopChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter loopChain = loopChainBuilder.build();

        egtbChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter egtbChain = egtbChainBuilder.build();

        quiescenceChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        quiescenceChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter quiescenceChain = withQuiescence ? quiescenceChainBuilder.build() : null;

        alphaBetaFlowControl.setQuiescenceNode(quiescenceChain);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);
        alphaBetaFlowControl.setLoopNode(loopChain);
        alphaBetaFlowControl.setLeafNode(leafChain);
        alphaBetaFlowControl.setEgtbNode(egtbChain);

        alphaBetaRootChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaRootChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        return alphaBetaRootChainBuilder.build();
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

