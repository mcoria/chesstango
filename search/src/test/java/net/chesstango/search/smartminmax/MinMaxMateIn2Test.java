package net.chesstango.search.smartminmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.MateIn2Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class MinMaxMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new MinMax();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
