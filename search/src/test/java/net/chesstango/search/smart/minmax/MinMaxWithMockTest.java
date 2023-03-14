package net.chesstango.search.smart.minmax;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class MinMaxWithMockTest {

    private GameMockEvaluator evaluator;

    private MinMax minMax;

    @Before
    public void setup() {
        minMax = new MinMax();
        evaluator = new GameMockEvaluator();
        minMax.setGameEvaluator(evaluator);
    }

    @Test
    public void testSingleMoveWhitePlays() {
        GameMock game = GameMockLoader.loadFromFile("SingleMove.json");

        SearchMoveResult searchResult = minMax.searchBestMove(game, 1);
        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(Square.b1, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.c3, bestMove.getTo().getSquare());
        Assert.assertEquals(1, searchResult.getEvaluation());
        Assert.assertEquals(3, game.getNodesVisited());
        Assert.assertEquals(3, evaluator.getNodesEvaluated());
    }

    @Test
    public void testSingleMoveBlackPlays() {
    }

    @Test
    public void testTwoMovesWhitePlays() {
    }

    @Test
    public void testTwoMovesBlackPlays() {
    }
}
