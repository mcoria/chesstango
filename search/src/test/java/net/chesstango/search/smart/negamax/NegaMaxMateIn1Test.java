package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.MateIn1Test;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn1Test extends MateIn1Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new GameEvaluatorByMaterial());
        this.searchMove = new NoIterativeDeepening(negaMax);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
