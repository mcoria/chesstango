package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.GenericTest;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaImp;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaGenericTest extends GenericTest {

    private SearchMove searchMove;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new GameEvaluatorByMaterial();

        Quiescence quiescence = new Quiescence();
        quiescence.setGameEvaluator(gameEvaluator);
        quiescence.setMoveSorter(moveSorter);
        quiescence.setNext(quiescence);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setGameEvaluator(gameEvaluator);
        alphaBetaImp.setNext(alphaBetaImp);

        AlphaBeta minMaxPruning = new AlphaBeta();
        minMaxPruning.setAlphaBetaSearch(alphaBetaImp);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBetaImp, quiescence, moveSorter));

        this.searchMove = new NoIterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }
}
