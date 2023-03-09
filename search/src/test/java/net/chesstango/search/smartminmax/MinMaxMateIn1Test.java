package net.chesstango.search.smartminmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.MateIn1Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class MinMaxMateIn1Test extends MateIn1Test {

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
