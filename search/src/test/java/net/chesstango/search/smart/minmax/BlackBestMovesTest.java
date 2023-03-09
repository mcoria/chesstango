package net.chesstango.search.smart.minmax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractBlackBestMovesTest;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class BlackBestMovesTest extends AbstractBlackBestMovesTest {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new IterativeDeeping();
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
