package net.chesstango.search.smartnegamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.MateIn1Test;
import net.chesstango.search.MateIn2Test;
import net.chesstango.search.SearchMove;
import org.junit.Before;

public class NegaMaxPrunningMateIn2Test extends MateIn2Test {

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
