package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.Search;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTrackerTest {

    private Search search;

    @BeforeEach
    public void setup() {
        this.search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .build();
    }

}
