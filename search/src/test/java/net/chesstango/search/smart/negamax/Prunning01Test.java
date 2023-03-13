package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.IterativeDeeping;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.Pruning01Test;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class Prunning01Test extends Pruning01Test {

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