package net.chesstango.search.smartminmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.MateIn4Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class MinMaxPrunningMateIn4Test extends MateIn4Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new MinMaxPruning();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
