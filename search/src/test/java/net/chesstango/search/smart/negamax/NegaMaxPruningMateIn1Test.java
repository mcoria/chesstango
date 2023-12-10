package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPruningMateIn1Test extends MateIn1Test {

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        NegaQuiescence negaQuiescence = new NegaQuiescence();
        negaQuiescence.setGameEvaluator(new EvaluatorByMaterial());
        negaQuiescence.setMoveSorter(moveSorter);

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);
        negaMaxPruning.setMoveSorter(moveSorter);

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();
        smartListenerMediator.addAll(List.of(moveSorter, negaMaxPruning));

        NoIterativeDeepening noIterativeDeepening = new NoIterativeDeepening(negaMaxPruning);
        noIterativeDeepening.setSmartListenerMediator(smartListenerMediator);
        noIterativeDeepening.setSearchParameter(SearchParameter.MAX_DEPTH,1);

        this.searchMove = noIterativeDeepening;
    }

}
