package net.chesstango.search.smart.alphabeta;

import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.MateIn3Test;
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
public class AlphaBetaMateIn3Test extends MateIn3Test {

    @BeforeEach
    public void setup() {
        this.searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .build();

        this.searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 5);
    }
}
