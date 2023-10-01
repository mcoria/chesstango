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
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoves;
import net.chesstango.search.smart.alphabeta.listeners.SetMoveEvaluations;
import net.chesstango.search.smart.alphabeta.listeners.SetPrincipalVariation;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
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
    private final FlowControl flowControl;
    private GameEvaluator gameEvaluator;
    private MoveSorter moveSorter;
    private MoveSorter qMoveSorter;
    private AlphaBetaFilter quiescence;
    private SetTranspositionTables setTranspositionTables;
    private AlphaBetaStatistics alphaBetaStatistics;
    private QuiescenceStatistics quiescenceStatistics;
    private TranspositionTable transpositionTable;
    private TranspositionTableQ transpositionTableQ;
    private StopProcessingCatch stopProcessingCatch;
    private ZobristTracker zobristTracker;
    private ZobristTracker zobristQTracker;

    private SetPrincipalVariation setPrincipalVariation;
    private SetMoveEvaluations setMoveEvaluations;
    private SetBestMoves setBestMoves;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;

    private boolean withZobristTracker;

    public AlphaBetaBuilder() {
        alphaBeta = new AlphaBeta();

        flowControl = new FlowControl();

        quiescence = new QuiescenceNull();

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
        quiescence = new Quiescence();
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
        if (quiescence instanceof QuiescenceNull) {
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
        if (transpositionTable == null && transpositionTableQ == null) {
            throw new RuntimeException("You must enable TranspositionTable first");
        }
        withZobristTracker = true;
        return this;
    }


    /**
     * AlphaBetaFacade -> StopProcessingCatch -> AlphaBetaStatistics -> TranspositionTable -> FlowControl -> AlphaBeta
     * *                                                     ^                                                |
     * *                                                     |                                                |
     * *                                                     -------------------------------------------------
     * StopProcessingCatch: al comienzo y solo una vez para atrapar excepciones de stop
     * AlphaBetaStatistics: al comienzo, para contabilizar los movimientos iniciales posibles
     * TranspositionTable: al comienzo, con iterative deeping tiene sentido dado que (DEPTH) + 4 puede repetir la misma posicion
     *
     * <p>
     * <p>
     * QuiescenceStatics -> QTranspositionTable -> Quiescence
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
            alphaBetaStatistics = new AlphaBetaStatistics();
        }

        if (withZobristTracker && transpositionTable != null) {
            zobristTracker = new ZobristTracker();
        }
        // ====================================================

        // =============  quiescence setup =====================
        if (quiescence instanceof Quiescence) {
            if (withStatistics) {
                quiescenceStatistics = new QuiescenceStatistics();
            }
            if (withZobristTracker && transpositionTableQ != null) {
                zobristQTracker = new ZobristTracker();
            }
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

        filterActions.add(flowControl);
        filterActions.add(alphaBeta);
        filterActions.add(quiescence);
        filterActions.add(moveSorter);
        filterActions.add(qMoveSorter);

        // =============  alphaBeta setup =====================
        if (withStatistics) {
            filterActions.add(alphaBetaStatistics);
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
        if (quiescence instanceof Quiescence) {
            if (quiescenceStatistics != null) {
                filterActions.add(quiescenceStatistics);
            }
            if (zobristQTracker != null) {
                filterActions.add(zobristQTracker);
            }
            if (transpositionTableQ != null) {
                filterActions.add(transpositionTableQ);
            }
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
        if (alphaBetaStatistics != null) {
            if (zobristTracker != null) {
                alphaBetaStatistics.setNext(zobristTracker);
            } else if (transpositionTable != null) {
                alphaBetaStatistics.setNext(transpositionTable);
            } else {
                alphaBetaStatistics.setNext(flowControl);
            }
            head = alphaBetaStatistics;
        }

        if (zobristTracker != null) {
            if (transpositionTable != null) {
                zobristTracker.setNext(transpositionTable);
            } else {
                zobristTracker.setNext(flowControl);
            }
            if (head == null) {
                head = zobristTracker;
            }
        }

        if (transpositionTable != null) {
            transpositionTable.setNext(flowControl);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = flowControl;
        }

        flowControl.setNext(alphaBeta);
        flowControl.setQuiescence(headQuiescence);
        flowControl.setGameEvaluator(gameEvaluator);

        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(head);
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

        // =============  quiescence setup =====================
        if (quiescence instanceof Quiescence realQuiescence) {
            realQuiescence.setMoveSorter(qMoveSorter);
            realQuiescence.setGameEvaluator(gameEvaluator);

            if (quiescenceStatistics != null) {
                if (zobristQTracker != null) {
                    quiescenceStatistics.setNext(zobristQTracker);
                } else if (transpositionTableQ != null) {
                    quiescenceStatistics.setNext(transpositionTableQ);
                } else {
                    quiescenceStatistics.setNext(quiescence);
                }
                headQuiescence = quiescenceStatistics;
            }

            if (zobristQTracker != null) {
                if (transpositionTableQ != null) {
                    zobristQTracker.setNext(transpositionTableQ);
                } else {
                    zobristQTracker.setNext(quiescence);
                }
                if (headQuiescence == null) {
                    headQuiescence = zobristQTracker;
                }
            }

            if (transpositionTableQ != null) {
                transpositionTableQ.setNext(quiescence);
                if (headQuiescence == null) {
                    headQuiescence = transpositionTableQ;
                }
            }

            if (headQuiescence == null) {
                headQuiescence = quiescence;
            }

            realQuiescence.setNext(headQuiescence);

        } else if (quiescence instanceof QuiescenceNull quiescenceNull) {
            quiescenceNull.setGameEvaluator(gameEvaluator);
            headQuiescence = quiescence;
        }

        return headQuiescence;
    }

}
