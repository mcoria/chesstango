package net.chesstango.search.smart.alphabeta;


import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.smart.MoveSorter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MinMaxPruningBuilder {

    private final AlphaBeta alphaBeta = new AlphaBeta();

    private final MoveSorter moveSorter = new MoveSorter();

    private AlphaBetaFilter quiescence = new QuiescenceNull();

    private GameEvaluator gameEvaluator = new DefaultGameEvaluator();

    private AlphaBetaStatistics alphaBetaStatistics = null;
    private DetectCycle detectCycle = null;

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
        return this;
    }


    /**
     * Statics -> DetectCycle -> AlphaBeta
     *
     * @return
     */
    public MinMaxPruning build() {
        List<AlphaBetaFilter> filters = new ArrayList<>();
        filters.add(alphaBeta);
        filters.add(quiescence);

        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);

        if (quiescence instanceof Quiescence) {
            ((Quiescence) quiescence).setMoveSorter(moveSorter);
            ((Quiescence) quiescence).setGameEvaluator(gameEvaluator);
        } else if (quiescence instanceof QuiescenceNull) {
            ((QuiescenceNull) quiescence).setGameEvaluator(gameEvaluator);
        }

        if (alphaBetaStatistics != null) {
            alphaBetaStatistics.setNext(detectCycle == null ? alphaBeta : detectCycle);
            filters.add(alphaBetaStatistics);
        }

        if (detectCycle != null) {
            detectCycle.setNext(alphaBeta);
            filters.add(detectCycle);
        }

        AlphaBetaFilter head;

        if (alphaBetaStatistics != null) {
            head = alphaBetaStatistics;
        } else if (detectCycle != null) {
            head = detectCycle;
        } else {
            head = alphaBeta;
        }

        alphaBeta.setNext(head);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(head);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(filters);

        return minMaxPruning;
    }


}
