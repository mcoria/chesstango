package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn2Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author Mauricio Coria
 */
public class MinMaxMateIn2Test extends MateIn2Test {

    private SearchMove searchMove;

    @BeforeEach
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
