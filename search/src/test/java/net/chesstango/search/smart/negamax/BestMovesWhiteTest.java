package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.smart.AbstractBestMovesWhiteTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BestMovesWhiteTest extends AbstractBestMovesWhiteTest {

    @BeforeEach
    public void setup() {
        NodeMoveSorter moveSorter = new NodeMoveSorter();
        moveSorter.setMoveComparator(new DefaultMoveComparator());

        Evaluator evaluator = new EvaluatorByMaterialPieces();

        NegaQuiescence negaQuiescence = new NegaQuiescence();
        negaQuiescence.setGameEvaluator(evaluator);
        negaQuiescence.setMoveSorter(moveSorter);

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);
        negaMaxPruning.setMoveSorter(moveSorter);

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.addAll(List.of(moveSorter, negaMaxPruning));

        IterativeDeepening iterativeDeepening = new IterativeDeepening(negaMaxPruning, smartListenerMediator);

        this.searchMove = iterativeDeepening;
    }
}
