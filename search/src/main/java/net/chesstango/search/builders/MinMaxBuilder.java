package net.chesstango.search.builders;

import net.chesstango.evaluation.DefaultEvaluator;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.minmax.MinMax;

/**
 * @author Mauricio Coria
 */
public class MinMaxBuilder implements SearchBuilder {

    private Evaluator evaluator = new DefaultEvaluator();

    @Override
    public SearchBuilder withGameEvaluator(Evaluator evaluator) {
        this.evaluator = evaluator;
        return this;
    }

    @Override
    public Search build() {
        MinMax minMax = new MinMax();
        minMax.setGameEvaluator(evaluator);
        return new NoIterativeDeepening(minMax, new SearchListenerMediator());
    }
}
