package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.once.AlphaBetaRoot;
import net.chesstango.search.smart.alphabeta.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.listeners.SetupGameEvaluator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class MoveEvaluationTrackerTest {

    private SearchMove searchMove;

    @BeforeEach
    public void setup() {
        this.searchMove = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())
                .build();
    }


    @Test
    public void testEvaluationCollisions01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 1);
        SearchMoveResult searchResult = searchMove.search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }

    @Test
    public void testEvaluationCollisions02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        searchMove.setSearchParameter(SearchParameter.MAX_DEPTH, 1);
        SearchMoveResult searchResult = searchMove.search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }
}
