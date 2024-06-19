package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.AbstractBestMovesBlackTest;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class BestMovesBlackTest extends AbstractBestMovesBlackTest {

    @BeforeEach
    public void setup() {
        this.searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterialPieces())
                .withQuiescence()
                .withIterativeDeepening()
                .build();
    }
}
