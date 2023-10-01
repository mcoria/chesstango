package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.smart.AbstractBlackBestMovesTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class BlackBestMovesTest extends AbstractBlackBestMovesTest {

    private SearchMove searchMove;

    @BeforeEach
    public void setup() {
        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        MoveSorter moveSorter = new DefaultMoveSorter();

        Quiescence quiescence = new Quiescence();
        quiescence.setGameEvaluator(gameEvaluator);
        quiescence.setMoveSorter(moveSorter);
        quiescence.setNext(quiescence);

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setMoveSorter(moveSorter);

        AlphaBetaFlowControl alphaBetaFlowControl =  new AlphaBetaFlowControl();
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);
        alphaBetaFlowControl.setNext(alphaBeta);

        alphaBeta.setNext(alphaBetaFlowControl);

        AlphaBetaFacade minMaxPruning = new AlphaBetaFacade();
        minMaxPruning.setAlphaBetaSearch(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl));

        this.searchMove = new IterativeDeepening(minMaxPruning);
    }


    @Override
    public SearchMove getBestMoveFinder() {
        return searchMove;
    }


}
