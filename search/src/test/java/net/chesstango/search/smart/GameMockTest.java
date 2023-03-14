package net.chesstango.search.smart;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Test;

public class GameMockTest {

    @Test
    public void search(){
        GameMock game = GameMockLoader.loadFromFile("GameGraph.json");
        GameMockEvaluator evaluator = new GameMockEvaluator();

        MinMax minMax = new MinMax();

        minMax.setGameEvaluator(evaluator);

        SearchMoveResult searchResult = minMax.searchBestMove(game, 4);
        Move bestMove = searchResult.getBestMove();

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(Square.c2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.b1, bestMove.getTo().getSquare());
        Assert.assertEquals(-1000, searchResult.getEvaluation());
        Assert.assertEquals(5, game.getNodesVisited());
        Assert.assertEquals(2, evaluator.getNodesEvaluated());
    }
}
