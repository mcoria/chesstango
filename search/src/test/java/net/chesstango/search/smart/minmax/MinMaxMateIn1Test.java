package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class MinMaxMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        MinMax searchMove = new MinMax();
        searchMove.setGameEvaluator(new EvaluatorByMaterial());
        this.searchMove = new NoIterativeDeepening(searchMove);
    }
}
