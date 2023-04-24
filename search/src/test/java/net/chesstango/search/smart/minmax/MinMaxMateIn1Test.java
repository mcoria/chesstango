package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.MateIn1Test;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class MinMaxMateIn1Test extends MateIn1Test {

    private SearchMove searchMove;

    @Before
    public void setup(){
        MinMax searchMove = new MinMax();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
        this.searchMove = new NoIterativeDeepening(searchMove);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
