package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.filters.once.AlphaBetaFirst;
import net.chesstango.search.smart.alphabeta.filters.once.MoveTracker;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoves;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class MoveTrackerTest {

    private AlphaBetaFacade alphaBetaFacade;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        AlphaBetaFirst alphaBetaFirst = new AlphaBetaFirst();
        MoveTracker moveTracker = new MoveTracker();
        AlphaBetaFlowControl alphaBetaFirstFlowControl = new AlphaBetaFlowControl();

        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();

        alphaBetaFirst.setNext(moveTracker);

        moveTracker.setNext(alphaBetaFirstFlowControl);
        moveTracker.setAlphaBetaFirst(alphaBetaFirst);

        alphaBetaFirstFlowControl.setNext(alphaBeta);
        alphaBetaFirstFlowControl.setQuiescence(quiescence);
        alphaBetaFirstFlowControl.setGameEvaluator(gameEvaluator);

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaFlowControl.setNext(alphaBeta);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaSearch(alphaBetaFirst);
        this.alphaBetaFacade.setSearchActions(Arrays.asList(alphaBetaFirst, moveTracker, quiescence, moveSorter, alphaBetaFirstFlowControl, alphaBeta, alphaBetaFlowControl, new SetBestMoves()));
    }


    @Test
    public void testEvaluationCollisions01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }

    @Test
    public void testEvaluationCollisions02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game);

        assertEquals(20, searchResult.getBestMovesCounter());
    }
}
