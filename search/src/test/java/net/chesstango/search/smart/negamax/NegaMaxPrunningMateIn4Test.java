package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn4Test;
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
