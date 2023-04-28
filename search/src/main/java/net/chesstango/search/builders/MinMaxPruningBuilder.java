package net.chesstango.search.builders;


import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruningBuilder implements SearchBuilder {

    private final AlphaBeta alphaBeta = new AlphaBeta();

    private final MoveSorter moveSorter = new MoveSorter();

    private AlphaBetaFilter quiescence = new QuiescenceNull();

    private GameEvaluator gameEvaluator = new DefaultGameEvaluator();

    private AlphaBetaStatistics alphaBetaStatistics = null;

    private QuiescenceStatics quiescenceStatics = null;

    private DetectCycle detectCycle = null;

    private TranspositionTable transpositionTable = null;

    private boolean withIterativeDeepening;

    public MinMaxPruningBuilder withIterativeDeepening(){
        this.withIterativeDeepening = true;
        return this;
    }

    @Override
    public MinMaxPruningBuilder withGameEvaluator(GameEvaluator gameEvaluator){
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
        alphaBetaStatistics = new AlphaBetaStatistics();
        quiescenceStatics = new QuiescenceStatics();
        return this;
    }

    public MinMaxPruningBuilder withTranspositionTable(){
        transpositionTable =  new TranspositionTable();
        return this;
    }

    /**
     * Statics -> DetectCycle -> TranspositionTable -> AlphaBeta
     *
     * @return
     */
    @Override
    public SearchMove build() {
        List<AlphaBetaFilter> filters = new ArrayList<>();
        filters.add(alphaBeta);
        filters.add(quiescence);

        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(gameEvaluator);

        if (quiescence instanceof Quiescence) {
            ((Quiescence) quiescence).setMoveSorter(moveSorter);
            ((Quiescence) quiescence).setGameEvaluator(gameEvaluator);
            ((Quiescence) quiescence).setNext(quiescenceStatics != null ? quiescenceStatics : quiescence);
        } else if (quiescence instanceof QuiescenceNull) {
            ((QuiescenceNull) quiescence).setGameEvaluator(gameEvaluator);
        }

        if(quiescenceStatics != null) {
            filters.add(quiescenceStatics);

            quiescenceStatics.setNext(quiescence);

            alphaBeta.setQuiescence(quiescenceStatics);

        } else {
            alphaBeta.setQuiescence(quiescence);
        }


        if (alphaBetaStatistics != null) {
            if (detectCycle != null) {
                alphaBetaStatistics.setNext(detectCycle);
            } else if (transpositionTable != null) {
                alphaBetaStatistics.setNext(transpositionTable);
            } else {
                alphaBetaStatistics.setNext(alphaBeta);
            }
            filters.add(alphaBetaStatistics);
        }

        if (detectCycle != null) {
            if (transpositionTable != null) {
                detectCycle.setNext(transpositionTable);
            } else {
                detectCycle.setNext(alphaBeta);
            }
            filters.add(detectCycle);
        }

        if (transpositionTable != null){
            transpositionTable.setNext(alphaBeta);
            filters.add(transpositionTable);
        }

        AlphaBetaFilter head;

        if (alphaBetaStatistics != null) {
            head = alphaBetaStatistics;
        } else if (detectCycle != null) {
            head = detectCycle;
        } else if (transpositionTable != null) {
            head = transpositionTable;
        } else {
            head = alphaBeta;
        }

        alphaBeta.setNext(head);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(head);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(filters);

        return withIterativeDeepening ? new IterativeDeepening(minMaxPruning): new NoIterativeDeepening(minMaxPruning);
    }


}
