package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.smart.MateIn2Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class MinMaxMateIn2Test extends MateIn2Test {

    @BeforeEach
    public void setup() {
        MinMax searchMove = new MinMax();
        searchMove.setGameEvaluator(new EvaluatorByMaterial());

        SearchListenerMediator searchListenerMediator = new SearchListenerMediator();
        searchListenerMediator.add(searchMove);

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(searchMove, searchListenerMediator);
        noIterativeDeepening.accept(new SetMaxDepthVisitor(4));

        this.search = noIterativeDeepening;
    }


}
