package net.chesstango.search.smartnegamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.commontests.MateIn4Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class NegaMaxPrunningMateIn4Test extends MateIn4Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new NegaMaxPruning();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
