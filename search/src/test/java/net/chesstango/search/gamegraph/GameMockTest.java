package net.chesstango.search.gamegraph;

import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMock;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author Mauricio Coria
 */
public class GameMockTest {

    @Test
    public void loadFromFile(){
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

    @Test(expected = RuntimeException.class)
    public void repeatedMoves(){
        String lines = "{\n" +
                "  \"fen\": \"rnbqkbnr/pppppppp/8/8/8/2N5/PPPPPPPP/R1BQKBNR b KQkq - 1 1\",\n" +
                "  \"links\": [\n" +
                "    {\n" +
                "      \"move\": \"d2d4\",\n" +
                "      \"node\": {\n" +
                "        \"fen\": \"rnbqkbnr/ppp1pppp/8/3p4/8/2N5/PPPPPPPP/R1BQKBNR w KQkq d6 0 2\",\n" +
                "        \"evaluation\": 1,\n" +
                "        \"status\": \"NO_CHECK\",\n" +
                "        \"links\": []\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"move\": \"d2d4\",\n" +
                "      \"node\": {\n" +
                "        \"fen\": \"rnbqkbnr/pppp1ppp/8/4p3/8/2N5/PPPPPPPP/R1BQKBNR w KQkq e6 0 2 \",\n" +
                "        \"evaluation\": 0,\n" +
                "        \"status\": \"NO_CHECK\",\n" +
                "        \"links\": []\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Reader reader = new StringReader(lines);
        new GameMockLoader().readGameMove(reader);
    }

}
