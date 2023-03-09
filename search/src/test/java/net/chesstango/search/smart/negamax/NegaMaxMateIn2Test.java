package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn2Test;
import org.junit.Before;

public class NegaMaxMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new NegaMax();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
