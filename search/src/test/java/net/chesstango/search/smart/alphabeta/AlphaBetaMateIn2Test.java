package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.MateIn2Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaMateIn2Test extends MateIn2Test {

    @BeforeEach
    public void setup() {
        this.searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterialPieces())
                .build();

        this.searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 3);
    }
}
