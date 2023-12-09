package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.MateIn4Test;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFlowControl;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.listeners.SetupGameEvaluator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaMateIn4Test extends MateIn4Test {


    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        SetupGameEvaluator setupGameEvaluator = new SetupGameEvaluator();

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaFlowControl.setNext(alphaBeta);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        quiescence.setGameEvaluator(gameEvaluator);

        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        SmartListenerMediator smartListenerMediator = new SmartListenerMediator();

        AlphaBetaFacade minMaxPruning = new AlphaBetaFacade();
        minMaxPruning.setAlphaBetaFilter(alphaBeta);

        smartListenerMediator.addAll(Arrays.asList(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl, setupGameEvaluator, minMaxPruning));

        NoIterativeDeepening noIterativeDeepening =  new NoIterativeDeepening(minMaxPruning);
        noIterativeDeepening.setSmartListenerMediator(smartListenerMediator);
        noIterativeDeepening.setParameter(SearchParameter.MAX_DEPTH, 7);

        this.searchMove = noIterativeDeepening;
    }

}
