package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AlgoWrapper;
import net.chesstango.search.smart.MateIn2Test;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new GameEvaluatorByMaterial());
        this.searchMove = new AlgoWrapper(negaMax);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
