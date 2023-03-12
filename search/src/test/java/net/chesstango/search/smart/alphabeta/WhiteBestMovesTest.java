package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractWhiteBestMovesTest;
import net.chesstango.search.smart.IterativeDeeping;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class WhiteBestMovesTest extends AbstractWhiteBestMovesTest {

    private SearchMove searchMove;

    @Before
    public void setup(){
        searchMove = new IterativeDeeping(new MinMaxPruning());
        searchMove.setGameEvaluator(new GameEvaluatorByMaterial());
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
