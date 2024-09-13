package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.MateIn1Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        this.search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .build();

        this.search.setSearchParameter(SearchParameter.MAX_DEPTH, 1);
    }


}
