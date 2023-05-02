package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.MateIn3Test;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.NoIterativeDeepening;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class MinMaxPrunningMateIn3Test extends MateIn3Test {

    private SearchMove searchMove;

    @BeforeEach
    public void setup(){
        MoveSorter moveSorter = new MoveSorter();

        GameEvaluator gameEvaluator = new GameEvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(alphaBeta);
        alphaBeta.setGameEvaluator(gameEvaluator);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, quiescence));

        this.searchMove = new NoIterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
