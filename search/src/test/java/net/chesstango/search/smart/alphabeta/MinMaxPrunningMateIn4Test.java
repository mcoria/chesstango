package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.MateIn4Test;
import net.chesstango.search.smart.MoveSorter;
import org.junit.Before;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunningMateIn4Test extends MateIn4Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        Quiescence quiescence = new Quiescence();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());
        quiescence.setMoveSorter(moveSorter);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, quiescence));

        this.searchMove = new NoIterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
