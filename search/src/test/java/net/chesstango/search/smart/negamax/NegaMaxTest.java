package net.chesstango.search.smart.negamax;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchResultByDepth;
import net.chesstango.search.SearchResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.gamegraph.MockEvaluator;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class NegaMaxTest {
    private MockEvaluator evaluator;
    private NegaMax negaMax;

    @BeforeEach
    public void setup() {
        negaMax = new NegaMax();
        evaluator = new MockEvaluator();
        negaMax.setGameEvaluator(evaluator);
    }

    @Test
    public void whiteTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn1Ply.json");

        SearchResult searchResult = search(game, 1);

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

        SearchResult searchResult = search(game, 1);

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

        SearchResult searchResult = search(game, 2);

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

        SearchResult searchResult = search(game, 2);

        Move bestMove = searchResult.getBestMove();

        assertNotNull(searchResult);
        assertEquals(Square.d7, bestMove.getFrom().getSquare());
        assertEquals(Square.d5, bestMove.getTo().getSquare());
        assertEquals(14, searchResult.getBestEvaluation());
        assertEquals(9, evaluator.getNodesEvaluated());
        assertEquals(12, game.getNodesVisited());
    }

    private SearchResult search(GameMock game, int depth) {
        SearchByCycleContext searchByCycleContext = new SearchByCycleContext(game);

        negaMax.beforeSearch(searchByCycleContext);

        SearchByDepthContext context = new SearchByDepthContext(depth);

        negaMax.beforeSearchByDepth(context);

        negaMax.search();

        negaMax.afterSearchByDepth(new SearchResultByDepth(depth));

        SearchResult searchResult = new SearchResult(depth);

        negaMax.afterSearch(searchResult);

        return searchResult;
    }
}
