package net.chesstango.search.smart.negamax;

import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractBlackBestMovesTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.MoveSorter;
import org.junit.Before;

/**
 * @author Mauricio Coria
 */
public class BlackBestMovesTest extends AbstractBlackBestMovesTest {

    private SearchMove searchMove;

    @Before
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        NegaQuiescence negaQuiescence = new NegaQuiescence(moveSorter);
        negaQuiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        NegaMaxPruning negaMaxPruning = new NegaMaxPruning(negaQuiescence);

        this.searchMove = new IterativeDeepening(negaMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
