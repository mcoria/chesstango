package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class MinMaxMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        MinMax searchMove = new MinMax();
        searchMove.setGameEvaluator(new EvaluatorByMaterial());

        SearchListenerMediator searchListenerMediator = new SearchListenerMediator();
        searchListenerMediator.add(searchMove);

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(searchMove, searchListenerMediator);
        noIterativeDeepening.setSearchParameter(SearchParameter.MAX_DEPTH, 1);

        this.search = noIterativeDeepening;
    }
}
