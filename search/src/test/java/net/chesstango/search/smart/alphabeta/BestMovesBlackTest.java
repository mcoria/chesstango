package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.smart.AbstractBestMovesBlackTest;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.Quiescence;
import net.chesstango.search.smart.alphabeta.listeners.SetupGameEvaluator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class BestMovesBlackTest extends AbstractBestMovesBlackTest {

    @BeforeEach
    public void setup() {
        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        MoveSorter moveSorter = new DefaultMoveSorter();

        Quiescence quiescence = new Quiescence();
        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        SetupGameEvaluator setupGameEvaluator = new SetupGameEvaluator();

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaFlowControl.setNext(alphaBeta);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        quiescence.setGameEvaluator(gameEvaluator);
        quiescence.setMoveSorter(moveSorter);
        quiescence.setNext(quiescence);

        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        AlphaBetaFacade minMaxPruning = new AlphaBetaFacade();
        minMaxPruning.setAlphaBetaFilter(alphaBeta);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl, setupGameEvaluator));

        this.searchMove = new IterativeDeepening(minMaxPruning);
    }
}
