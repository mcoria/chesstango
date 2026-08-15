package net.chesstango.search.builders;


import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchBuilder;
import net.chesstango.search.builders.alphabeta.*;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.core.listeners.SetSearchTimers;
import net.chesstango.search.smart.alphabeta.core.visitors.LinkBestMovesArray;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTracker;
import net.chesstango.search.smart.alphabeta.debug.DebugNodeTrap;
import net.chesstango.search.smart.alphabeta.debug.iterators.PrintHtmlDebugHandler;
import net.chesstango.search.smart.alphabeta.debug.visitors.LinkDebugNodeTrapVisitor;
import net.chesstango.search.smart.alphabeta.debug.visitors.LinkSearchTrackerVisitor;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBaseNull;
import net.chesstango.search.smart.alphabeta.egtb.liteners.SetGameToEndGameTableBase;
import net.chesstango.search.smart.alphabeta.egtb.visitors.LinkEndGameTableBaseVisitor;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;
import net.chesstango.search.smart.alphabeta.pv.visitors.LinkTrianglePVVisitor;
import net.chesstango.search.smart.alphabeta.root.filters.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.statistics.game.DepthCollector;
import net.chesstango.search.smart.alphabeta.statistics.game.GameCountersCollector;
import net.chesstango.search.smart.alphabeta.statistics.node.NodeCounters;
import net.chesstango.search.smart.alphabeta.statistics.node.visitors.LinkNodeCountersVisitor;
import net.chesstango.search.smart.alphabeta.zobrist.listeners.SetZobristMemory;
import net.chesstango.search.smart.sorters.visitors.LinkMoveToHashMap;

import static net.chesstango.search.smart.Constants.*;

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
    private final KillerMoveBuilder killerMoveBuilder;
    private final EvaluationBuilder evaluationBuilder;
    private final EgtbChainBuilder egtbChainBuilder;

    private final SetGameToEndGameTableBase setGameToEndGameTableBase;

    private final AlphaBetaFacade alphaBetaFacade;
    private final SearchListenerMediator searchListenerMediator;
    private final AlphaBetaFlowControl alphaBetaFlowControl;

    private NodeCounters nodeCounters;
    private GameCountersCollector gameCounters;
    private DepthCollector depthCollector;
    private SetZobristMemory setZobristMemory;
    private PrintHtmlDebugHandler printHtmlDebugHandler;

    private DebugNodeTrap debugNodeTrap;
    private DebugNodeTracker debugNodeTracker;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTable;
    private boolean withZobristTracker;
    private boolean withQuiescence;
    private boolean withExtensionCheckResolver;
    private boolean withDebugSearchTree;
    private boolean withAspirationWindows;
    private boolean withKillerMoveSorter;

    private Search search;

    public AlphaBetaBuilder() {
        alphaBetaRootChainBuilder = new AlphaBetaRootChainBuilder();
        alphaBetaInteriorChainBuilder = new AlphaBetaInteriorChainBuilder();

        quiescenceChainBuilder = new QuiescenceChainBuilder();
        checkResolverChainBuilder = new CheckResolverChainBuilder();
        transpositionTableBuilder = new TranspositionTableBuilder();
        killerMoveBuilder = new KillerMoveBuilder();
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
        alphaBetaRootChainBuilder.withIterativeDeepening();
        alphaBetaInteriorChainBuilder.withIterativeDeepening();
        quiescenceChainBuilder.withIterativeDeepening();
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

    @Override
    public AlphaBetaBuilder withTranspositionTable() {
        alphaBetaRootChainBuilder.withTranspositionTable();
        alphaBetaInteriorChainBuilder.withTranspositionTable();

        quiescenceChainBuilder.withTranspositionTable();
        checkResolverChainBuilder.withTranspositionTable();

        withTranspositionTable = true;
        return this;
    }

    @Override
    public AlphaBetaBuilder withTranspositionHashSize(int hashSizeKB) {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        transpositionTableBuilder.withHashSize(hashSizeKB);
        return this;
    }


    @Override
    public AlphaBetaBuilder withTranspositionStaleAge(int staleAge) {
        if (!withTranspositionTable) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        transpositionTableBuilder.withStaleAge(staleAge);
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
        if (!withDebugSearchTree) {
            throw new RuntimeException("You must enable DebugSearchTree first");
        }
        this.debugNodeTrap = debugNodeTrap;
        return this;
    }

    public AlphaBetaBuilder withDebugSearchTree() {
        alphaBetaRootChainBuilder.withDebugSearchTree();
        alphaBetaInteriorChainBuilder.withDebugSearchTree();
        terminalChainBuilder.withDebugSearchTree();
        loopChainBuilder.withDebugSearchTree();
        leafChainBuilder.withDebugSearchTree();
        egtbChainBuilder.withDebugSearchTree();
        transpositionTableBuilder.withDebugSearchTree();
        killerMoveBuilder.withDebugSearchTree();

        quiescenceChainBuilder.withDebugSearchTree();
        checkResolverChainBuilder.withDebugSearchTree();

        evaluationBuilder.withDebugSearchTree();

        this.withDebugSearchTree = true;
        return this;
    }

    @Override
    public Search build() {
        buildObjects();

        setupListenerMediator();

        link();

        return search;
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

        if (withKillerMoveSorter) {
            killerMoveBuilder.withSmartListenerMediator(searchListenerMediator);
            killerMoveBuilder.build();
        }

        if (withStatistics) {
            nodeCounters = new NodeCounters();
            gameCounters = new GameCountersCollector();

            depthCollector = new DepthCollector();
        }

        if (withZobristTracker) {
            setZobristMemory = new SetZobristMemory();
        }

        if (withDebugSearchTree) {
            debugNodeTracker = new DebugNodeTracker();

            printHtmlDebugHandler = new PrintHtmlDebugHandler();
        }
    }


    private void setupListenerMediator() {
        searchListenerMediator.add(setGameToEndGameTableBase);

        searchListenerMediator.add(alphaBetaFacade);

        searchListenerMediator.add(setSearchTimers);

        searchListenerMediator.add(alphaBetaFlowControl);

        if (setZobristMemory != null) {
            searchListenerMediator.add(setZobristMemory);
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

        if (debugNodeTracker != null) {
            searchListenerMediator.add(debugNodeTracker);
        }

        if (debugNodeTrap != null) {
            searchListenerMediator.add(debugNodeTrap);
        }

        if (printHtmlDebugHandler != null) {
            searchListenerMediator.add(printHtmlDebugHandler);
        }
    }

    private void link() {
        alphaBetaFacade.setNext(createChain());
        alphaBetaFacade.setSearchListenerMediator(searchListenerMediator);

        if (withTranspositionTable) {
            transpositionTableBuilder.link();
        }

        if (withKillerMoveSorter) {
            killerMoveBuilder.link();
        }

        if (withStatistics) {
            searchListenerMediator.accept(new LinkNodeCountersVisitor(nodeCounters));
        }

        if (withDebugSearchTree) {
            if (debugNodeTrap != null) {
                searchListenerMediator.accept(new LinkDebugNodeTrapVisitor(debugNodeTrap));
            }
            searchListenerMediator.accept(new LinkSearchTrackerVisitor(debugNodeTracker));
        }

        /**
         * Link Builders
         */
        evaluationBuilder.link();

        terminalChainBuilder.link();
        leafChainBuilder.link();
        alphaBetaInteriorChainBuilder.link();
        loopChainBuilder.link();
        egtbChainBuilder.link();
        if (withQuiescence) {
            quiescenceChainBuilder.link();
        }
        alphaBetaRootChainBuilder.link();

        /**
         * Link through the mediator
         */

        searchListenerMediator.accept(new LinkMoveToHashMap(new MoveToHashMap()));

        searchListenerMediator.accept(new LinkBestMovesArray(new Move[MAX_DEPTH]));

        searchListenerMediator.accept(new LinkEndGameTableBaseVisitor(new EndGameTableBaseNull()));

        searchListenerMediator.accept(new LinkTrianglePVVisitor(new TriangularPVTable()));
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
                .withTranspositionHashSize(DEFAULT_HASH_SIZE_KB)
                .withTranspositionStaleAge(DEFAULT_STALE_AGE)

                .withTranspositionMoveSorter()
                .withKillerMoveSorter()
                .withRecaptureSorter()
                .withMvvLvaSorter()

                .withAspirationWindows()

                .withIterativeDeepening()

                .withStopProcessingCatch();
    }
}

