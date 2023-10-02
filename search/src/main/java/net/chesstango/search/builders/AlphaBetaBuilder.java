package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.listeners.*;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;
import net.chesstango.search.smart.statistics.GameStatisticsListener;
import net.chesstango.search.smart.statistics.IterativeWrapper;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaBuilder implements SearchBuilder {
    private final AlphaBeta alphaBeta;
    private final AlphaBetaFlowControl alphaBetaFlowControl;
    private GameEvaluator gameEvaluator;
    private MoveSorter moveSorter;
    private MoveSorter qMoveSorter;
    private QuiescenceNull quiescenceNull;
    private Quiescence quiescence;
    private QuiescenceFlowControl quiescenceFlowControl;
    private SetTranspositionTables setTranspositionTables;
    private AlphaBetaStatisticsExpected alphaBetaStatisticsExpected;
    private AlphaBetaStatisticsVisited alphaBetaStatisticsVisited;
    private QuiescenceStatisticsExpected quiescenceStatisticsExpected;
    private QuiescenceStatisticsVisited quiescenceStatisticsVisited;
    private TranspositionTable transpositionTable;
    private TranspositionTableQ transpositionTableQ;
    private StopProcessingCatch stopProcessingCatch;
    private ZobristTracker zobristTracker;
    private ZobristTracker zobristQTracker;
    private SetPrincipalVariation setPrincipalVariation;
    private SetMoveEvaluations setMoveEvaluations;
    private SetBestMoves setBestMoves;
    private SetNodeStatistics setNodeStatistics;

    private boolean withQuiescence;
    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;
    private boolean withZobristTracker;

    public AlphaBetaBuilder() {
        alphaBeta = new AlphaBeta();

        alphaBetaFlowControl = new AlphaBetaFlowControl();

        gameEvaluator = new DefaultEvaluator();

        moveSorter = new DefaultMoveSorter();

        qMoveSorter = new DefaultMoveSorter();
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
        this.withQuiescence = true;
        return this;
    }

    public AlphaBetaBuilder withStatistics() {
        this.withStatistics = true;
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionTable() {
        if (!withQuiescence) {
            throw new RuntimeException("You must enable Quiescence first");
        }
        transpositionTableQ = new TranspositionTableQ();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        if (transpositionTable == null) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        moveSorter = new TranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionMoveSorter() {
        if (transpositionTableQ == null) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        qMoveSorter = new TranspositionMoveSorterQ();
        return this;
    }

    public AlphaBetaBuilder withStopProcessingCatch() {
        stopProcessingCatch = new StopProcessingCatch();
        return this;
    }


    public AlphaBetaBuilder withTranspositionTableReuse() {
        if (transpositionTable == null & transpositionTableQ == null) {
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
        return this;
    }


    /**
     * AlphaBetaFacade -> StopProcessingCatch ->
     * *
     * * AlphaBetaStatistics -> ZobristTracker -> TranspositionTable -> AlphaBetaFlowControl -> AlphaBeta
     * *            ^                                                                            |
     * *            |                                                                            |
     * *            -----------------------------------------------------------------------------
     * *
     * StopProcessingCatch: al comienzo y solo una vez para atrapar excepciones de stop
     * AlphaBetaStatistics: al comienzo, para contabilizar los movimientos iniciales posibles
     * TranspositionTable: al comienzo, con iterative deeping tiene sentido dado que se puede repetir la misma busqueda
     *
     * <p>
     * <p>
     * *  QuiescenceStatics -> ZobristTracker -> TranspositionTableQ -> QuiescenceFlowControl -> Quiescence
     * *            ^                                                                              |
     * *            |                                                                              |
     * *            -------------------------------------------------------------------------------
     *
     * @return
     */
    @Override
    public SearchMove build() {
        buildObjects();

        List<SearchLifeCycle> searchActions = createSearchActions();

        AlphaBetaFilter head = createChain();

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

        if (transpositionTable != null || transpositionTableQ != null) {
            setTranspositionTables = new SetTranspositionTables();
            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }
        }

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            setNodeStatistics = new SetNodeStatistics();
            alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
            alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        }

        if (withZobristTracker) {
            zobristTracker = new ZobristTracker();
        }
        // ====================================================

        // =============  quiescence setup =====================
        if (withQuiescence) {
            quiescence = new Quiescence();
            quiescenceFlowControl = new QuiescenceFlowControl();
            if (withStatistics) {
                quiescenceStatisticsExpected = new QuiescenceStatisticsExpected();
                quiescenceStatisticsVisited = new QuiescenceStatisticsVisited();
            }
            if (withZobristTracker) {
                zobristQTracker = new ZobristTracker();
            }
        } else {
            quiescenceNull = new QuiescenceNull();
        }
        // ====================================================

        setPrincipalVariation = new SetPrincipalVariation();
        if (withStatistics && transpositionTable != null) {
            setMoveEvaluations = new SetMoveEvaluations();
            setBestMoves = new SetBestMoves();
        }
    }


    private List<SearchLifeCycle> createSearchActions() {
        List<SearchLifeCycle> filterActions = new LinkedList<>();

        if (setTranspositionTables != null) {
            // Este filtro necesita agregarse primero
            filterActions.add(setTranspositionTables);
        }


        filterActions.add(moveSorter);
        filterActions.add(alphaBeta);
        filterActions.add(alphaBetaFlowControl);

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            filterActions.add(setNodeStatistics);
            filterActions.add(alphaBetaStatisticsExpected);
            filterActions.add(alphaBetaStatisticsVisited);
        }

        if (zobristTracker != null) {
            filterActions.add(zobristTracker);
        }

        if (transpositionTable != null) {
            filterActions.add(transpositionTable);
        }

        if (stopProcessingCatch != null) {
            filterActions.add(stopProcessingCatch);
        }
        // ====================================================

        // =============  quiescence setup =====================
        if (withQuiescence) {
            filterActions.add(qMoveSorter);
            filterActions.add(quiescence);
            filterActions.add(quiescenceFlowControl);
            if (withStatistics) {
                filterActions.add(quiescenceStatisticsExpected);
                filterActions.add(quiescenceStatisticsVisited);
            }
            if (zobristQTracker != null) {
                filterActions.add(zobristQTracker);
            }
            if (transpositionTableQ != null) {
                filterActions.add(transpositionTableQ);
            }
        } else {
            filterActions.add(quiescenceNull);
        }
        // ====================================================

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            filterActions.add(evaluatorStatistics);
        }

        filterActions.add(setPrincipalVariation);
        if (setMoveEvaluations != null) {
            filterActions.add(setMoveEvaluations);
        }

        if (setBestMoves != null) {
            filterActions.add(setBestMoves);
        }

        return filterActions;
    }


    private AlphaBetaFilter createChain() {
        AlphaBetaFilter headQuiescence = createQuiescenceChain();

        // =============  alphaBeta setup =====================

        AlphaBetaFilter head = null;
        if (zobristTracker != null) {
            if (transpositionTable != null) {
                zobristTracker.setNext(transpositionTable);
            } else {
                zobristTracker.setNext(alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta);
            }
            head = zobristTracker;
        }

        if (transpositionTable != null) {
            transpositionTable.setNext(alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = alphaBetaStatisticsExpected != null ? alphaBetaStatisticsExpected : alphaBeta;
        }


        if (alphaBetaStatisticsExpected != null) {
            alphaBetaStatisticsExpected.setNext(alphaBeta);
        }

        alphaBeta.setNext(alphaBetaStatisticsVisited != null ? alphaBetaStatisticsVisited : alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);

        if (alphaBetaStatisticsVisited != null) {
            alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);
        }

        alphaBetaFlowControl.setNext(head);
        alphaBetaFlowControl.setQuiescence(headQuiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        // ====================================================

        // StopProcessingCatch is set one in the chain
        if (stopProcessingCatch != null) {
            stopProcessingCatch.setNext(head);
            head = stopProcessingCatch;
        }

        return head;
    }

    private AlphaBetaFilter createQuiescenceChain() {
        AlphaBetaFilter headQuiescence = null;

        if (withQuiescence) {
            if (zobristQTracker != null) {
                if (transpositionTableQ != null) {
                    zobristQTracker.setNext(transpositionTableQ);
                } else {
                    zobristQTracker.setNext(quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence);
                }
                headQuiescence = zobristQTracker;
            }

            if (transpositionTableQ != null) {
                transpositionTableQ.setNext(quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence);
                if (headQuiescence == null) {
                    headQuiescence = transpositionTableQ;
                }
            }

            if (headQuiescence == null) {
                headQuiescence = quiescenceStatisticsExpected != null ? quiescenceStatisticsExpected : quiescence;
            }

            if (quiescenceStatisticsExpected != null) {
                quiescenceStatisticsExpected.setNext(quiescence);
            }

            quiescence.setNext(quiescenceStatisticsVisited != null ? quiescenceStatisticsVisited : quiescenceFlowControl);
            quiescence.setMoveSorter(qMoveSorter);
            quiescence.setGameEvaluator(gameEvaluator);


            if (quiescenceStatisticsVisited != null) {
                quiescenceStatisticsVisited.setNext(quiescenceFlowControl);
            }

            quiescenceFlowControl.setNext(headQuiescence);
            quiescenceFlowControl.setGameEvaluator(gameEvaluator);

        } else {
            quiescenceNull.setGameEvaluator(gameEvaluator);
            headQuiescence = quiescenceNull;
        }

        return headQuiescence;
    }

}
