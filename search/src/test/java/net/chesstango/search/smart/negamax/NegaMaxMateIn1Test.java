package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class NegaMaxMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        NegaMax negaMax = new NegaMax();
        negaMax.setGameEvaluator(new EvaluatorByMaterial());

        SearchListenerMediator searchListenerMediator = new SearchListenerMediator();
        searchListenerMediator.add(negaMax);

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(negaMax, searchListenerMediator);
        noIterativeDeepening.setSearchParameter(SearchParameter.MAX_DEPTH,1);

        this.search = noIterativeDeepening;
    }
}
