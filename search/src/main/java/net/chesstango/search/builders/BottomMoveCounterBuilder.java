package net.chesstango.search.builders;


import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.builders.alphabeta.*;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.BottomMoveCounterFacade;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.evaluator.listeners.SetGameToEvaluator;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchLast;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetDebugOutput;
import net.chesstango.search.smart.alphabeta.debug.listeners.SetSearchTracker;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTablesDebug;
import net.chesstango.search.smart.alphabeta.killermoves.listeners.SetKillerMoveTables;
import net.chesstango.search.smart.alphabeta.statistics.evaluation.EvaluatorStatisticsCollector;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.smart.alphabeta.transposition.listeners.TranspositionTableListener;

/**
 * @author Mauricio Corias
 */
public class BottomMoveCounterBuilder implements SearchBuilder {
    private final SetSearchTimers setSearchTimers;
    private final SetSearchLast setSearchLast;
    private final AlphaBetaInteriorChainBuilder alphaBetaInteriorChainBuilder;
    private final TerminalChainBuilder terminalChainBuilder;
    private final TerminalChainBuilder quiescenceTerminalChainBuilder;
    private final LoopChainBuilder loopChainBuilder;
    private final LoopChainBuilder quiescenceLoopChainBuilder;
    private final QuiescenceChainBuilder quiescenceChainBuilder;
    private final LeafChainBuilder quiescenceLeafChainBuilder;
    private final LeafChainBuilder leafChainBuilder;
    private final CheckResolverChainBuilder checkResolverChainBuilder;
    private final SetGameToEvaluator setGameToEvaluator;
    private final BottomMoveCounterFacade bottomMoveCounterFacade;
    private final SearchListenerMediator searchListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private Evaluator evaluator;
    private EvaluatorCache gameEvaluatorCache;
    private EvaluatorStatisticsCollector gameEvaluatorStatisticsCollector;
    private TranspositionTableListener resetTranspositionTables;
    private SetKillerMoveTablesDebug setKillerMoveTablesDebug;
    private NodeCounters nodeCounters;
    private SetDebugOutput setDebugOutput;
    private SetSearchTracker setSearchTracker;
    private SetKillerMoveTables setKillerMoveTables;
    private DebugNodeTrap debugNodeTrap;

    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withTrackEvaluations;
    private boolean withGameEvaluatorCache;
    private boolean withQuiescence;
    private boolean withExtensionCheckResolver;
    private boolean withPrintChain;
    private boolean withDebugSearchTree;
    private boolean showNodeTranspositionAccess;
    private boolean showSorterOperations;
    private boolean withKillerMoveSorter;

    public BottomMoveCounterBuilder() {
        alphaBetaInteriorChainBuilder = new AlphaBetaInteriorChainBuilder();

        quiescenceChainBuilder = new QuiescenceChainBuilder();


        checkResolverChainBuilder = new CheckResolverChainBuilder();

        bottomMoveCounterFacade = new BottomMoveCounterFacade();
        setGameToEvaluator = new SetGameToEvaluator();
        searchListenerMediator = new SearchListenerMediator();
        alphaBetaFlowControl = new AlphaBetaFlowControl();

        setSearchTimers = new SetSearchTimers();
        setSearchLast = new SetSearchLast();

        terminalChainBuilder = new TerminalChainBuilder();
        quiescenceTerminalChainBuilder = new TerminalChainBuilder();

        leafChainBuilder = new LeafChainBuilder();
        quiescenceLeafChainBuilder = new LeafChainBuilder();

        loopChainBuilder = new LoopChainBuilder();
        quiescenceLoopChainBuilder = new LoopChainBuilder();
    }


    @Override
    public BottomMoveCounterBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    public BottomMoveCounterBuilder withGameEvaluatorCache() {
        withGameEvaluatorCache = true;
        return this;
    }

    public BottomMoveCounterBuilder withQuiescence() {
        withQuiescence = true;
        return this;
    }

    public BottomMoveCounterBuilder withExtensionCheckResolver() {
        withExtensionCheckResolver = true;
        return this;
    }

    public BottomMoveCounterBuilder withStatistics() {
        withStatistics = true;
        alphaBetaInteriorChainBuilder.withStatistics();
        quiescenceChainBuilder.withStatistics();
        checkResolverChainBuilder.withStatistics();
        return this;
    }

    public BottomMoveCounterBuilder withTranspositionTable() {
        withTranspositionTable = true;
        alphaBetaInteriorChainBuilder.withTranspositionTable();
        terminalChainBuilder.withTranspositionTable();


        quiescenceChainBuilder.withTranspositionTable();
        checkResolverChainBuilder.withTranspositionTable();
        quiescenceTerminalChainBuilder.withTranspositionTable();
        return this;
    }

    public BottomMoveCounterBuilder withTranspositionMoveSorter() {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        alphaBetaInteriorChainBuilder.withTranspositionMoveSorter();
        quiescenceChainBuilder.withTranspositionMoveSorter();
        return this;
    }

    public BottomMoveCounterBuilder withTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable Statistics first");
        }
        withTrackEvaluations = true;
        return this;
    }

    public BottomMoveCounterBuilder withPrintChain() {
        withPrintChain = true;
        return this;
    }

    public BottomMoveCounterBuilder withKillerMoveSorter() {
        alphaBetaInteriorChainBuilder.withKillerMoveSorter();
        withKillerMoveSorter = true;
        return this;
    }

    public BottomMoveCounterBuilder withRecaptureSorter() {
        alphaBetaInteriorChainBuilder.withRecaptureSorter();
        quiescenceChainBuilder.withRecaptureSorter();
        return this;
    }

    public BottomMoveCounterBuilder withMvvLvaSorter() {
        alphaBetaInteriorChainBuilder.withMvvLvaSorter();
        quiescenceChainBuilder.withMvvLvaSorter();
        return this;
    }

    public BottomMoveCounterBuilder withDebugNodeTrap(DebugNodeTrap debugNodeTrap) {
        this.debugNodeTrap = debugNodeTrap;
        return this;
    }

    public BottomMoveCounterBuilder withDebugSearchTree(boolean showOnlyPV, boolean showNodeTranspositionAccess, boolean showSorterOperations) {
        alphaBetaInteriorChainBuilder.withDebugSearchTree();
        terminalChainBuilder.withDebugSearchTree();
        loopChainBuilder.withDebugSearchTree();
        leafChainBuilder.withDebugSearchTree();

        quiescenceChainBuilder.withDebugSearchTree();
        quiescenceLeafChainBuilder.withDebugSearchTree();
        checkResolverChainBuilder.withDebugSearchTree();
        quiescenceTerminalChainBuilder.withDebugSearchTree();
        quiescenceLoopChainBuilder.withZobristTracker();

        this.withDebugSearchTree = true;
        this.showNodeTranspositionAccess = showNodeTranspositionAccess;
        this.showSorterOperations = showSorterOperations;
        return this;
    }

    @Override
    public Search build() {
        buildObjects();

        setupListenerMediatorBeforeChain();

        bottomMoveCounterFacade.setAlphaBetaFilter(createChain());

        setupListenerMediatorAfterChain();

        return new NoIterativeDeepening(bottomMoveCounterFacade, searchListenerMediator);
    }

    private void buildObjects() {
        if (withGameEvaluatorCache) {
            gameEvaluatorCache = new EvaluatorCache(evaluator);

            evaluator = gameEvaluatorCache;
        }

        if (withStatistics) {
            gameEvaluatorStatisticsCollector = new EvaluatorStatisticsCollector()
                    .setImp(evaluator)
                    .setGameEvaluatorCache(gameEvaluatorCache)
                    .setTrackEvaluations(withTrackEvaluations);

            evaluator = gameEvaluatorStatisticsCollector;
        }

        if (withTranspositionTable) {
            resetTranspositionTables = new TranspositionTableListener();
        }

        if (withStatistics) {
            nodeCounters = new NodeCounters();
        }

        if (withDebugSearchTree) {
            setSearchTracker = new SetSearchTracker();
            setDebugOutput = new SetDebugOutput(false, false, showNodeTranspositionAccess, showSorterOperations);
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
        searchListenerMediator.add(setGameToEvaluator);

        searchListenerMediator.add(bottomMoveCounterFacade);

        searchListenerMediator.add(setSearchTimers);

        searchListenerMediator.add(setSearchLast);

        if (setSearchTracker != null) {
            searchListenerMediator.add(setSearchTracker);
        }

        if (resetTranspositionTables != null) {
            searchListenerMediator.add(resetTranspositionTables);
        }

        if (nodeCounters != null) {
            searchListenerMediator.add(nodeCounters);
        }

        if (gameEvaluatorStatisticsCollector != null) {
            searchListenerMediator.add(gameEvaluatorStatisticsCollector);
        }

        if (setKillerMoveTables != null) {
            searchListenerMediator.add(setKillerMoveTables);
        } else if (setKillerMoveTablesDebug != null) {
            searchListenerMediator.add(setKillerMoveTablesDebug);
            searchListenerMediator.add(setKillerMoveTablesDebug.getKillerMovesDebug());
        }

        searchListenerMediator.add(alphaBetaFlowControl);
    }

    private void setupListenerMediatorAfterChain() {
        if (debugNodeTrap instanceof SearchListener debugNodeTrapSearchListener) {
            searchListenerMediator.add(debugNodeTrapSearchListener);
        } else if (debugNodeTrap instanceof Acceptor debugNodeTrapAcceptor) {
            searchListenerMediator.add(debugNodeTrapAcceptor);
        }
        if (setDebugOutput != null) {
            searchListenerMediator.add(setDebugOutput);
        }
    }


    private AlphaBetaFilter createChain() {
        setGameToEvaluator.setEvaluator(evaluator);

        terminalChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter terminalChain = terminalChainBuilder.build();

        leafChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter leafChain = leafChainBuilder.build();


        alphaBetaInteriorChainBuilder.withSmartListenerMediator(searchListenerMediator);
        alphaBetaInteriorChainBuilder.withAlphaBetaFlowControl(alphaBetaFlowControl);
        alphaBetaInteriorChainBuilder.withGameEvaluatorCache(gameEvaluatorCache);
        AlphaBetaFilter interiorChain = alphaBetaInteriorChainBuilder.build();

        loopChainBuilder.withSmartListenerMediator(searchListenerMediator);
        AlphaBetaFilter loopChain = loopChainBuilder.build();

        alphaBetaFlowControl.setQuiescenceNode(null);
        alphaBetaFlowControl.setInteriorNode(interiorChain);
        alphaBetaFlowControl.setTerminalNode(terminalChain);
        alphaBetaFlowControl.setLoopNode(loopChain);
        alphaBetaFlowControl.setLeafNode(leafChain);

        return alphaBetaFlowControl;
    }


    public static BottomMoveCounterBuilder createDefaultBuilderInstance(final Evaluator evaluator) {
        return new BottomMoveCounterBuilder()
                .withGameEvaluator(evaluator)
                .withGameEvaluatorCache()

                .withQuiescence()

                .withTranspositionTable()

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter();
    }
}

