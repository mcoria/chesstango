package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn3Test;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.NoIterativeDeepening;


/**
 * @author Mauricio Coria
 */
public class NegaMaxPrunningMateIn3Test extends MateIn3Test {

    private SearchMove searchMove;

    @BeforeEach
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        NegaQuiescence negaQuiescence = new NegaQuiescence(moveSorter);
        negaQuiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);

        this.searchMove = new NoIterativeDeepening(negaMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
