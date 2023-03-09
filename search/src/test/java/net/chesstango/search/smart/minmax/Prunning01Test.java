package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.Pruning01Test;
import net.chesstango.search.smart.negamax.NegaMaxPruning;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class Prunning01Test extends Pruning01Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new MinMaxPruning();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
