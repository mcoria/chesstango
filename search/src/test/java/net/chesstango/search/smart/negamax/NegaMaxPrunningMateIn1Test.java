package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn1Test;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.alphabeta.AlphaBetaImp;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.alphabeta.Quiescence;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class NegaMaxPrunningMateIn1Test extends MateIn1Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        NegaQuiescence negaQuiescence = new NegaQuiescence(moveSorter);
        negaQuiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);

        this.searchMove = negaMaxPruning;
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}