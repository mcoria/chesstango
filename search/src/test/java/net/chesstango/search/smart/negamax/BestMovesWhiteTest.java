package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractBestMovesWhiteTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BestMovesWhiteTest extends AbstractBestMovesWhiteTest {

    @BeforeEach
    public void setup() {
        DefaultMoveSorter moveSorter = new DefaultMoveSorter();
        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        NegaQuiescence negaQuiescence = new NegaQuiescence();
        negaQuiescence.setGameEvaluator(gameEvaluator);
        negaQuiescence.setMoveSorter(moveSorter);

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);
        negaMaxPruning.setMoveSorter(moveSorter);

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.addAll(List.of(moveSorter, negaMaxPruning));

        IterativeDeepening iterativeDeepening = new IterativeDeepening(negaMaxPruning, smartListenerMediator);

        this.searchMove = iterativeDeepening;
    }
}
