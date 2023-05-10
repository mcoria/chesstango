package net.chesstango.search.builders;

import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.minmax.MinMax;

/**
 * @author Mauricio Coria
 */
public class MinMaxBuilder implements SearchBuilder {

    private GameEvaluator gameEvaluator = new DefaultGameEvaluator();

    @Override
    public SearchBuilder withGameEvaluator(GameEvaluator gameEvaluator) {
        this.gameEvaluator = gameEvaluator;
        return this;
    }

    @Override
    public SearchMove build() {
        MinMax minMax = new MinMax();
        minMax.setGameEvaluator(gameEvaluator);
        return new NoIterativeDeepening(minMax);
    }
}
