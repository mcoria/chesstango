package net.chesstango.search.smart.minmax;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.SearchContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class MinMaxTest {

    private GameMockEvaluator evaluator;

    private MinMax minMax;

    @Before
    public void setup() {
        minMax = new MinMax();
        evaluator = new GameMockEvaluator();
        minMax.setGameEvaluator(evaluator);
    }

    @Test
    public void whiteTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn1Ply.json");

        SearchMoveResult searchResult = minMax.searchBestMove(game, new SearchContext(1));
        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(Square.b1, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.c3, bestMove.getTo().getSquare());
        Assert.assertEquals(1, searchResult.getEvaluation());
        Assert.assertEquals(3, evaluator.getNodesEvaluated());
        Assert.assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void blackTurn1Ply() {
        GameMock game = GameMockLoader.loadFromFile("BlackTurn1Ply.json");

        SearchMoveResult searchResult = minMax.searchBestMove(game, new SearchContext(1));
        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(Square.b8, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.c6, bestMove.getTo().getSquare());
        Assert.assertEquals(-1, searchResult.getEvaluation());
        Assert.assertEquals(3, evaluator.getNodesEvaluated());
        Assert.assertEquals(3, game.getNodesVisited());
    }

    @Test
    public void whiteTurn2Ply() {
        GameMock game = GameMockLoader.loadFromFile("WhiteTurn2Ply.json");

        SearchMoveResult searchResult = minMax.searchBestMove(game, new SearchContext(2));
        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(Square.d2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.d4, bestMove.getTo().getSquare());
        Assert.assertEquals(5, searchResult.getEvaluation());
        Assert.assertEquals(6, evaluator.getNodesEvaluated());
        Assert.assertEquals(8, game.getNodesVisited());
    }

    @Test
    @Ignore
    public void blackTurn2Ply() {
        throw new RuntimeException("Implement");
    }
}
