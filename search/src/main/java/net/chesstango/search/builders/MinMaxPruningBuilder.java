package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.*;
import net.chesstango.search.smart.alphabeta.GameEvaluatorCounter;
import net.chesstango.search.smart.alphabeta.*;
import net.chesstango.search.smart.movesorters.DefaultMoveSorter;
import net.chesstango.search.smart.movesorters.MoveSorter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruningBuilder implements SearchBuilder {

    private final AlphaBeta alphaBeta = new AlphaBeta();

    private final MoveSorter moveSorter = new DefaultMoveSorter();

    private AlphaBetaFilter quiescence = new QuiescenceNull();

    private GameEvaluator gameEvaluator = new DefaultGameEvaluator();

    private AlphaBetaStatistics alphaBetaStatistics = null;

    private QuiescenceStatics quiescenceStatics = null;

    private DetectCycle detectCycle = null;

    private TranspositionTable transpositionTable = null;

    private QTranspositionTable qTranspositionTable = null;

    private boolean withIterativeDeepening;
    private boolean withStatics;

    public MinMaxPruningBuilder withIterativeDeepening() {
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MinMaxPruningBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    public MinMaxPruningBuilder withQuiescence() {
        quiescence = new Quiescence();
        return this;
    }

    public MinMaxPruningBuilder withDetectCycle() {
        detectCycle = new DetectCycle();
        return this;
    }

    public MinMaxPruningBuilder withStatics() {
        this.withStatics = true;
        return this;
    }

    public MinMaxPruningBuilder withTranspositionTable() {
        transpositionTable = new TranspositionTable();
        return this;
    }

    public MinMaxPruningBuilder withQTranspositionTable() {
        qTranspositionTable = new QTranspositionTable();
        return this;
    }

    /**
     * MinMaxPruning -> Statics -> DetectCycle -> TranspositionTable -> AlphaBeta
     *                     ^                                                |
     *                     |                                                |
     *                     -------------------------------------------------
     *
     *
     * QuiescenceStatics -> QTranspositionTable -> Quiescence
     *
     *
     *
     * @return
     */
    @Override
    public SearchMove build() {
        if(withStatics){
            alphaBetaStatistics = new AlphaBetaStatistics();
            quiescenceStatics = new QuiescenceStatics();
            gameEvaluator = new GameEvaluatorCounter(gameEvaluator);
        }

        List<FilterActions> filters = new ArrayList<>();
        filters.add(alphaBeta);
        filters.add(quiescence);
        filters.add(moveSorter);
        if(gameEvaluator instanceof GameEvaluatorCounter) {
            filters.add((GameEvaluatorCounter)gameEvaluator);
        }

        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(gameEvaluator);

        // =============  quiescence setup =====================
        AlphaBetaFilter headQuiescence = null;
        if (quiescence instanceof Quiescence) {
            Quiescence realQuiescence = (Quiescence) quiescence;
            realQuiescence.setMoveSorter(moveSorter);
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

        alphaBeta.setQuiescence(headQuiescence);
        // ===================================

        // =============  alphaBeta setup =====================
        AlphaBetaFilter head = null;
        if (alphaBetaStatistics != null) {
            filters.add(alphaBetaStatistics);
            if (detectCycle != null) {
                alphaBetaStatistics.setNext(detectCycle);
            } else if (transpositionTable != null) {
                alphaBetaStatistics.setNext(transpositionTable);
            } else {
                alphaBetaStatistics.setNext(alphaBeta);
            }
            head = alphaBetaStatistics;
        }

        if (detectCycle != null) {
            filters.add(detectCycle);
            if (transpositionTable != null) {
                detectCycle.setNext(transpositionTable);
            } else {
                detectCycle.setNext(alphaBeta);
            }
            if (head == null) {
                head = detectCycle;
            }
        }

        if (transpositionTable != null) {
            filters.add(transpositionTable);
            transpositionTable.setNext(alphaBeta);
            if (head == null) {
                head = transpositionTable;
            }
        }

        if (head == null) {
            head = alphaBeta;
        }

        alphaBeta.setNext(head);
        // ===================================

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(head);
        minMaxPruning.setFilters(filters);

        return withIterativeDeepening ? new IterativeDeepening(minMaxPruning) : new NoIterativeDeepening(minMaxPruning);
    }


}
