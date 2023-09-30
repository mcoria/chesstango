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
import net.chesstango.search.smart.sorters.TranspositionMoveSorterQ;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
import net.chesstango.search.smart.statistics.GameStatisticsListener;
import net.chesstango.search.smart.statistics.IterativeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaBuilder implements SearchBuilder {

    private final AlphaBeta alphaBeta;

    private MoveSorter moveSorter;

    private MoveSorter qMoveSorter;

    private AlphaBetaFilter quiescence;

    private GameEvaluator gameEvaluator;

    private AlphaBetaStatistics alphaBetaStatistics;

    private QuiescenceStatistics quiescenceStatistics;

    private TranspositionTable transpositionTable;

    private TranspositionTableQ transpositionTableQ;

    private StopProcessingCatch stopProcessingCatch;

    private ZobristTracker zobristTracker;
    private ZobristTracker zobristQTracker;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withTranspositionTableReuse;
    private boolean withTrackEvaluations;

    public AlphaBetaBuilder() {
        alphaBeta = new AlphaBeta();

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
        if (transpositionTable != null) {
            zobristTracker = new ZobristTracker();
        }
        if (transpositionTableQ != null) {
            zobristQTracker = new ZobristTracker();
        }
        return this;
    }


    /**
     * MinMaxPruning -> StopProcessingCatch -> AlphaBetaStatistics -> TranspositionTable -> AlphaBeta
     * *                                                   ^                                    |
     * *                                                   |                                    |
     * *                                                   -------------------------------------
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

        if (withStatistics) {
            alphaBetaStatistics = new AlphaBetaStatistics();

            quiescenceStatistics = new QuiescenceStatistics();

            gameEvaluator = new EvaluatorStatistics(gameEvaluator)
                    .setTrackEvaluations(withTrackEvaluations);
        }

        List<SearchLifeCycle> filters = new ArrayList<>();

        // ====================================================
        if (transpositionTable != null || transpositionTableQ != null) {
            SetTranspositionTables setTranspositionTables = new SetTranspositionTables();

            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }

            // Este filtro necesita agregarse primero
            filters.add(setTranspositionTables);
        }
        // ====================================================

        filters.add(alphaBeta);
        filters.add(quiescence);
        filters.add(moveSorter);
        filters.add(qMoveSorter);

        if (gameEvaluator instanceof EvaluatorStatistics evaluatorStatistics) {
            filters.add(evaluatorStatistics);
        }

        // =============  quiescence setup =====================
        AlphaBetaFilter headQuiescence = null;
        if (quiescence instanceof Quiescence realQuiescence) {
            realQuiescence.setMoveSorter(qMoveSorter);
            realQuiescence.setGameEvaluator(gameEvaluator);

            if (quiescenceStatistics != null) {
                filters.add(quiescenceStatistics);
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
                filters.add(zobristQTracker);
                zobristQTracker.setNext(transpositionTableQ);
                if (headQuiescence == null) {
                    headQuiescence = zobristQTracker;
                }
            }

            if (transpositionTableQ != null) {
                filters.add(transpositionTableQ);
                transpositionTableQ.setNext(quiescence);
                if (headQuiescence == null) {
                    headQuiescence = transpositionTableQ;
                }
            }

            if (headQuiescence == null) {
                headQuiescence = quiescence;
            }

            realQuiescence.setNext(headQuiescence);

        } else if (quiescence instanceof QuiescenceNull) {
            ((QuiescenceNull) quiescence).setGameEvaluator(gameEvaluator);
            headQuiescence = quiescence;
        }
        // ====================================================

        // =============  alphaBeta setup =====================
        AlphaBetaFilter head = null;
        if (alphaBetaStatistics != null) {
            filters.add(alphaBetaStatistics);
            if (zobristTracker != null) {
                alphaBetaStatistics.setNext(zobristTracker);
            } else if (transpositionTable != null) {
                alphaBetaStatistics.setNext(transpositionTable);
            } else {
                alphaBetaStatistics.setNext(this.alphaBeta);
            }
            head = alphaBetaStatistics;
        }

        if (zobristTracker != null) {
            filters.add(zobristTracker);
            zobristTracker.setNext(transpositionTable);
            if (head == null) {
                head = zobristTracker;
            }
        }

        if (transpositionTable != null) {
            filters.add(transpositionTable);
            transpositionTable.setNext(this.alphaBeta);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = this.alphaBeta;
        }

        this.alphaBeta.setMoveSorter(moveSorter);
        this.alphaBeta.setGameEvaluator(gameEvaluator);
        this.alphaBeta.setQuiescence(headQuiescence);
        this.alphaBeta.setNext(head);

        // ====================================================

        // GameRevert is set one in the chain
        if (stopProcessingCatch != null) {
            filters.add(stopProcessingCatch);

            stopProcessingCatch.setNext(head);

            head = stopProcessingCatch;
        }

        // ====================================================

        filters.add(new SetPrincipalVariation());

        if (withStatistics && transpositionTable != null) {
            filters.add(new SetMoveEvaluations());

            filters.add(new SetBestMoves());
        }

        //TTDump ttDump = new TTDump();
        //filters.add(ttDump);


        //TTLoad ttLoad = new TTLoad();
        //filters.add(ttLoad);

        // ====================================================

        AlphaBetaFacade alphaBetaFacade = new AlphaBetaFacade();
        alphaBetaFacade.setAlphaBetaSearch(head);
        alphaBetaFacade.setSearchActions(filters);

        SearchMove searchMove;
        if (withIterativeDeepening) {
            searchMove = new IterativeDeepening(alphaBetaFacade);
        } else {
            searchMove = new NoIterativeDeepening(alphaBetaFacade);
        }

        if (withStatistics) {
            IterativeWrapper iterativeWrapper = new IterativeWrapper(searchMove);

            filters.add(new GameStatisticsListener());

            searchMove = iterativeWrapper;
        }

        return searchMove;
    }

}
