package net.chesstango.search.smartminmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.commontests.MateIn1Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class MinMaxPrunningMateIn1Test extends MateIn1Test {

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
