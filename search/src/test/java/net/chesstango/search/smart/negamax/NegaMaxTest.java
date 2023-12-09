package net.chesstango.search.smart.negamax;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class NegaMaxTest {
    private GameMockEvaluator evaluator;
    private NegaMax negaMax;

    @BeforeEach
    public void setup() {
        negaMax = new NegaMax();
        evaluator = new GameMockEvaluator();
        negaMax.setGameEvaluator(evaluator);
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
        negaMax.beforeSearch(game);

        SearchContext context = new SearchContext(depth);

        negaMax.beforeSearchByDepth(context);

        SearchMoveResult result = negaMax.search();

        negaMax.afterSearchByDepth(result);

        negaMax.afterSearch(result);

        return result;
    }
}
