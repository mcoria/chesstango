package net.chesstango.search.smart.minmax;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.MoveEvaluation;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class MinMaxTest {

    private GameMockEvaluator evaluator;

    private MinMax minMax;

    @BeforeEach
    public void setup() {
        minMax = new MinMax();
        evaluator = new GameMockEvaluator();
        minMax.setGameEvaluator(evaluator);
    }

    @Test
    public void whiteTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn1Ply.json");

        SearchMoveResult searchResult = search(game, 1);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.b1, bestMove.getFrom().getSquare());
        assertEquals(Square.c3, bestMove.getTo().getSquare());
        assertEquals(1, searchResult.getBestEvaluation());
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
        assertEquals(-1, searchResult.getBestEvaluation());
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
        assertEquals(5, searchResult.getBestEvaluation());
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
        assertEquals(14, searchResult.getBestEvaluation());
        assertEquals(9, evaluator.getNodesEvaluated());
        assertEquals(12, game.getNodesVisited());
    }

    private SearchMoveResult search(GameMock game, int depth) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        minMax.beforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(depth);

        minMax.beforeSearchByDepth(context);

        MoveEvaluation bestMoveEvaluation = minMax.search();

        SearchMoveResult searchResult = new SearchMoveResult(depth, bestMoveEvaluation, null);

        minMax.afterSearchByDepth(searchResult);

        minMax.afterSearch();

        return searchResult;
    }

}
