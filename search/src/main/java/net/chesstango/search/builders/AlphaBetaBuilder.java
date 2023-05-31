package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchLifeCycle;
import net.chesstango.search.smart.alphabeta.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.listeners.SetMoveEvaluations;
import net.chesstango.search.smart.alphabeta.listeners.SearchSetup;
import net.chesstango.search.smart.alphabeta.listeners.SetPrincipalVariation;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.QTranspositionMoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;

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

    private QuiescenceStatics quiescenceStatics;

    private TranspositionTable transpositionTable;

    private QTranspositionTable qTranspositionTable;

    private StopProcessingCatch stopProcessingCatch;

    private boolean withIterativeDeepening;
    private boolean withStatics;
    private boolean withMoveEvaluation;

    public AlphaBetaBuilder() {
        alphaBetaImp = new AlphaBetaImp();

        quiescence = new QuiescenceNull();

        gameEvaluator = new DefaultGameEvaluator();

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

    public AlphaBetaBuilder withStatics() {
        this.withStatics = true;
        return this;
    }

    public AlphaBetaBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionTable() {
        qTranspositionTable = new QTranspositionTable();
        return this;
    }

    public AlphaBetaBuilder withTranspositionMoveSorter() {
        moveSorter = new TranspositionMoveSorter();
        return this;
    }

    public AlphaBetaBuilder withQTranspositionMoveSorter() {
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

        if (withStatics) {
            alphaBetaStatistics = new AlphaBetaStatistics();
            quiescenceStatics = new QuiescenceStatics();
            gameEvaluator = new GameEvaluatorCounter(gameEvaluator);
        }

        List<SearchLifeCycle> filters = new ArrayList<>();
        filters.add(new SearchSetup());
        filters.add(alphaBetaImp);
        filters.add(quiescence);
        filters.add(moveSorter);
        filters.add(qMoveSorter);

        if (gameEvaluator instanceof GameEvaluatorCounter) {
            filters.add((GameEvaluatorCounter) gameEvaluator);
        }

        // =============  quiescence setup =====================
        AlphaBetaFilter headQuiescence = null;
        if (quiescence instanceof Quiescence) {
            Quiescence realQuiescence = (Quiescence) quiescence;
            realQuiescence.setMoveSorter(qMoveSorter);
            realQuiescence.setGameEvaluator(gameEvaluator);

            if (quiescenceStatics != null) {
                filters.add(quiescenceStatics);
                if (qTranspositionTable != null) {
                    quiescenceStatics.setNext(qTranspositionTable);
                } else {
                    quiescenceStatics.setNext(quiescence);
                }
                headQuiescence = quiescenceStatics;
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
        if(withMoveEvaluation) {
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

        return withIterativeDeepening ? new IterativeDeepening(alphaBeta) : new NoIterativeDeepening(alphaBeta);
    }


}
