package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractWhiteBestMovesTest;
import net.chesstango.search.smart.IterativeDeeping;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class WhiteBestMovesTest extends AbstractWhiteBestMovesTest {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new IterativeDeeping(new NegaMaxPruning());
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
