package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.smart.AbstractBestMovesBlackTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class BestMovesBlackTest extends AbstractBestMovesBlackTest {

    @BeforeEach
    public void setup() {
        NodeMoveSorter moveSorter = new NodeMoveSorter();
        moveSorter.setMoveComparator(new DefaultMoveComparator());

        Evaluator evaluator = new EvaluatorByMaterial();

        NegaQuiescence negaQuiescence = new NegaQuiescence();
        negaQuiescence.setGameEvaluator(evaluator);
        negaQuiescence.setMoveSorter(moveSorter);

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);
        negaMaxPruning.setMoveSorter(moveSorter);

        SearchListenerMediator searchListenerMediator = new SearchListenerMediator();
        searchListenerMediator.addAllAcceptor(List.of(moveSorter, negaMaxPruning));

        IterativeDeepening iterativeDeepening = new IterativeDeepening(negaMaxPruning, searchListenerMediator);

        this.search = iterativeDeepening;
    }
}
