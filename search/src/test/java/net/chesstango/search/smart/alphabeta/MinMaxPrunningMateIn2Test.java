package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn2Test;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.NoIterativeDeepening;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunningMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @BeforeEach
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        Quiescence quiescence = new Quiescence();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());
        quiescence.setMoveSorter(moveSorter);

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(alphaBeta);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        this.searchMove = new NoIterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
