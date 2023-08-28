package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.listeners.SetMoveEvaluations;
import net.chesstango.search.smart.alphabeta.listeners.SetPrincipalVariation;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.QTranspositionMoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
import net.chesstango.search.smart.statistics.IterativeWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaBuilder implements SearchBuilder {

    private final AlphaBetaImp alphaBetaImp;

    private MoveSorter moveSorter;

    private MoveSorter qMoveSorter;

    private AlphaBetaFilter quiescence;

    private GameEvaluator gameEvaluator;

    private AlphaBetaStatistics alphaBetaStatistics;

    private QuiescenceStatistics quiescenceStatistics;

    private TranspositionTable transpositionTable;

    private QTranspositionTable qTranspositionTable;

    private StopProcessingCatch stopProcessingCatch;

    private boolean withIterativeDeepening;
    private boolean withStatistics;
    private boolean withMoveEvaluation;
    private boolean withTranspositionTableReuse;
    private boolean withStatisticsTrackEvaluations;

    public AlphaBetaBuilder() {
        alphaBetaImp = new AlphaBetaImp();

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
        qTranspositionTable = new QTranspositionTable();
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
        if (qTranspositionTable == null) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }

        qMoveSorter = new QTranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withStopProcessingCatch() {
        stopProcessingCatch = new StopProcessingCatch();
        return this;
    }


    public AlphaBetaBuilder withMoveEvaluation() {
        withMoveEvaluation = true;
        return this;
    }

    public AlphaBetaBuilder withTranspositionTableReuse() {
        withTranspositionTableReuse = true;
        return this;
    }

    public SearchBuilder withStatisticsTrackEvaluations() {
        if (!withStatistics) {
            throw new RuntimeException("You must enable QTranspositionTable first");
        }
        withStatisticsTrackEvaluations = true;
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
                    .setTrackEvaluations(withStatisticsTrackEvaluations);
        }

        List<SearchLifeCycle> filters = new ArrayList<>();

        // ====================================================
        if (transpositionTable != null || qTranspositionTable != null) {
            SetTranspositionTables setTranspositionTables = new SetTranspositionTables();

            if (withTranspositionTableReuse) {
                setTranspositionTables.setReuseTranspositionTable(true);
            }

            // Este filtro necesita agregarse primero
            filters.add(setTranspositionTables);
        }
        // ====================================================

        filters.add(alphaBetaImp);
        filters.add(quiescence);
        filters.add(moveSorter);
        filters.add(qMoveSorter);

        if (gameEvaluator instanceof EvaluatorStatistics) {
            filters.add((EvaluatorStatistics) gameEvaluator);
        }

        // =============  quiescence setup =====================
        AlphaBetaFilter headQuiescence = null;
        if (quiescence instanceof Quiescence realQuiescence) {
            realQuiescence.setMoveSorter(qMoveSorter);
            realQuiescence.setGameEvaluator(gameEvaluator);

            if (quiescenceStatistics != null) {
                filters.add(quiescenceStatistics);
                if (qTranspositionTable != null) {
                    quiescenceStatistics.setNext(qTranspositionTable);
                } else {
                    quiescenceStatistics.setNext(quiescence);
                }
                headQuiescence = quiescenceStatistics;
            }

            if (qTranspositionTable != null) {
                filters.add(qTranspositionTable);
                qTranspositionTable.setNext(quiescence);
                if (headQuiescence == null) {
                    headQuiescence = qTranspositionTable;
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
            if (transpositionTable != null) {
                alphaBetaStatistics.setNext(transpositionTable);
            } else {
                alphaBetaStatistics.setNext(this.alphaBetaImp);
            }
            head = alphaBetaStatistics;
        }

        if (transpositionTable != null) {
            filters.add(transpositionTable);
            transpositionTable.setNext(this.alphaBetaImp);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = this.alphaBetaImp;
        }


        this.alphaBetaImp.setMoveSorter(moveSorter);
        this.alphaBetaImp.setGameEvaluator(gameEvaluator);
        this.alphaBetaImp.setQuiescence(headQuiescence);
        this.alphaBetaImp.setNext(head);

        // ====================================================

        // GameRevert is set one in the chain
        if (stopProcessingCatch != null) {
            filters.add(stopProcessingCatch);

            stopProcessingCatch.setNext(head);

            head = stopProcessingCatch;
        }

        // ====================================================
        if (withMoveEvaluation) {
            if (transpositionTable == null) {
                throw new RuntimeException("SetMoveEvaluations requires transpositionTable");
            } else {
                filters.add(new SetMoveEvaluations());
            }
        }

        filters.add(new SetPrincipalVariation());

        //TTDump ttDump = new TTDump();
        //filters.add(ttDump);


        //TTLoad ttLoad = new TTLoad();
        //filters.add(ttLoad);

        // ====================================================

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setAlphaBetaSearch(head);
        alphaBeta.setSearchActions(filters);

        SearchMove searchMove;
        if (withIterativeDeepening) {

            IterativeWrapper iterativeWrapper = new IterativeWrapper(new IterativeDeepening(alphaBeta));

            filters.add(iterativeWrapper);

            searchMove = iterativeWrapper;
        } else {
            searchMove = new NoIterativeDeepening(alphaBeta);
        }

        return searchMove;
    }

}
