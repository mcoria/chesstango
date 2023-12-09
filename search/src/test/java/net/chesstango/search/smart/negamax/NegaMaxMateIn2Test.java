package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn2Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn2Test extends MateIn2Test {

    @BeforeEach
    public void setup() {
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new EvaluatorByMaterial());

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.add(negaMax);

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(negaMax);
        noIterativeDeepening.setSmartListenerMediator(smartListenerMediator);
        noIterativeDeepening.setParameter(SearchParameter.MAX_DEPTH,3);

        this.searchMove = noIterativeDeepening;
    }


}
