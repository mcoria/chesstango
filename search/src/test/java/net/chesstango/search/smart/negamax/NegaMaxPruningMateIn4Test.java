package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterialPieces;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn4Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.sorters.NodeMoveSorter;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruningMateIn4Test extends MateIn4Test {

    @BeforeEach
    public void setup() {
        NodeMoveSorter moveSorter = new NodeMoveSorter();
        moveSorter.setMoveComparator(new DefaultMoveComparator());

        NegaQuiescence negaQuiescence = new NegaQuiescence();
        negaQuiescence.setGameEvaluator(new EvaluatorByMaterialPieces());
        negaQuiescence.setMoveSorter(moveSorter);

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);
        negaMaxPruning.setMoveSorter(moveSorter);

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.addAll(List.of(moveSorter, negaMaxPruning));

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(negaMaxPruning, smartListenerMediator);
        noIterativeDeepening.setSearchParameter(SearchParameter.MAX_DEPTH, 7);

        this.searchMove = noIterativeDeepening;
    }
}
