package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractWhiteBestMovesTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class WhiteBestMovesTest extends AbstractWhiteBestMovesTest {

    private SearchMove searchMove;

    @BeforeEach
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        GameEvaluator gameEvaluator = new GameEvaluatorByMaterial();

        Quiescence quiescence = new Quiescence();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());
        quiescence.setMoveSorter(moveSorter);
        quiescence.setNext(quiescence);

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(gameEvaluator);
        alphaBeta.setNext(alphaBeta);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        this.searchMove = new IterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
