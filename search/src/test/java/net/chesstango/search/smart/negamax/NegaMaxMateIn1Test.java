package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new EvaluatorByMaterialPieces());

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.add(negaMax);

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(negaMax,smartListenerMediator);
        noIterativeDeepening.setSearchParameter(SearchParameter.MAX_DEPTH,1);

        this.searchMove = noIterativeDeepening;
    }
}
