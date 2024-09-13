package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.AbstractBestMovesBlackTest;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class BestMovesBlackTest extends AbstractBestMovesBlackTest {

    @BeforeEach
    public void setup() {
        this.search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .withQuiescence()
                .withIterativeDeepening()
                .build();
    }
}
