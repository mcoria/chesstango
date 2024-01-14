package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.listeners.SetGameEvaluator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class AlphaBetaTest {


    private GameMockEvaluator evaluator;

    private AlphaBetaFacade alphaBetaFacade;

    private SmartListenerMediator smartListenerMediator;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        evaluator = new GameMockEvaluator();

        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaEvaluation terminal = new AlphaBetaEvaluation();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        QuiescenceNull quiescence = new QuiescenceNull();
        SetGameEvaluator setGameEvaluator = new SetGameEvaluator();

        alphaBeta.setNext(alphaBetaFlowControl);
        alphaBeta.setMoveSorter(moveSorter);


        alphaBetaFlowControl.setTerminalNode(new AlphaBetaEvaluation());
        alphaBetaFlowControl.setHorizonNode(terminal);
        alphaBetaFlowControl.setTerminalNode(terminal);
        alphaBetaFlowControl.setInteriorNode(alphaBeta);

        quiescence.setGameEvaluator(evaluator);
        terminal.setGameEvaluator(evaluator);

        setGameEvaluator.setGameEvaluator(evaluator);

        this.smartListenerMediator = new SmartListenerMediator();

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaFilter(alphaBeta);

        this.smartListenerMediator.addAll(Arrays.asList(alphaBeta, quiescence, moveSorter, alphaBetaFlowControl, setGameEvaluator, alphaBetaFacade));
    }

    @Test
    public void whiteTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn1Ply.json");

        SearchMoveResult searchResult = search(game, 1);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.b1, bestMove.getFrom().getSquare());
        assertEquals(Square.c3, bestMove.getTo().getSquare());
        assertEquals(1, searchResult.getEvaluation());
        assertEquals(3, evaluator.getNodesEvaluated());
        assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void blackTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("BlackTurn1Ply.json");

        SearchMoveResult searchResult = search(game, 1);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.b8, bestMove.getFrom().getSquare());
        assertEquals(Square.c6, bestMove.getTo().getSquare());
        assertEquals(-1, searchResult.getEvaluation());
        assertEquals(3, evaluator.getNodesEvaluated());
        assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void whiteTurn2Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn2Ply.json");

        SearchMoveResult searchResult = search(game, 2);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.d2, bestMove.getFrom().getSquare());
        assertEquals(Square.d4, bestMove.getTo().getSquare());
        assertEquals(5, searchResult.getEvaluation());
        assertEquals(6, evaluator.getNodesEvaluated());
        assertEquals(8, game.getNodesVisited());
    }

    @Test
    public void blackTurn2Ply() {
        GameMock game = GameMockLoader.loadFromFile("BlackTurn2Ply.json");

        SearchMoveResult searchResult = search(game, 2);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.d7, bestMove.getFrom().getSquare());
        assertEquals(Square.d5, bestMove.getTo().getSquare());
        assertEquals(14, searchResult.getEvaluation());
        assertEquals(9, evaluator.getNodesEvaluated());
        assertEquals(12, game.getNodesVisited());
    }

    private SearchMoveResult search(GameMock game, int depth) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        smartListenerMediator.triggerBeforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(depth);

        smartListenerMediator.triggerBeforeSearchByDepth(context);

        MoveEvaluation bestMoveEvaluation = alphaBetaFacade.search();

        SearchMoveResult searchResult = new SearchMoveResult(depth, bestMoveEvaluation.evaluation(), bestMoveEvaluation.move(), null);

        smartListenerMediator.triggerAfterSearchByDepth(searchResult);

        smartListenerMediator.triggerAfterSearch();

        return searchResult;
    }
}
