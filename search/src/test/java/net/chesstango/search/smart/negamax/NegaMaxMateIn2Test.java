package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn2Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @BeforeEach
    public void setup() {
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new EvaluatorByMaterial());
        this.searchMove = new NoIterativeDeepening(negaMax);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
