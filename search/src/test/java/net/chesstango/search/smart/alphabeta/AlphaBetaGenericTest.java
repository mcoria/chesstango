package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.GenericTest;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaGenericTest extends GenericTest {
    @BeforeEach
    public void setup() {
        this.search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())

                .withQuiescence()

                .build();

        this.search.accept(new SetMaxDepthVisitor(1));
    }
}
