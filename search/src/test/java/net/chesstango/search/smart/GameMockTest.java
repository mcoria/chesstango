package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.gamegraph.GameMockEvaluator;
import net.chesstango.search.gamegraph.GameMockLoader;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Test;

public class GameMockTest {

    @Test
    public void search(){
        Game game = GameMockLoader.loadFromFile();

        MinMax minMax = new MinMax();

        minMax.setGameEvaluator(new GameMockEvaluator());

        SearchMoveResult searchResult = minMax.searchBestMove(game, 4);

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(1, searchResult.getEvaluation());
    }
}
