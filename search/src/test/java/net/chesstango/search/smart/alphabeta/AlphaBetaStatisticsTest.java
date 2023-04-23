package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private MinMaxPruning minMaxPruning;

    @Before
    public void setup() {
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaStatistics);

        alphaBetaStatistics.setNext(alphaBetaImp);

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, alphaBetaStatistics, quiescence));
    }

    @Test
    public void testDistinctMoves() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(2)));

        Set<Move>[] distinctMoves = searchResult.getDistinctMovesPerLevel();

        Assert.assertEquals(20, distinctMoves[0].size());
        Assert.assertEquals(20, distinctMoves[1].size());
    }

    @Test
    public void testVisitedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(2)));

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();

        Assert.assertEquals(20, visitedNodesCounters[0]);
        Assert.assertEquals(39, visitedNodesCounters[1]); // ESTA PERFECTO ES 39!!!!
    }

    @Test
    public void testExpectedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(2)));

        int[] visitedNodesCounters = searchResult.getExpectedNodesCounters();

        Assert.assertEquals(20, visitedNodesCounters[0]);
        Assert.assertEquals(400, visitedNodesCounters[1]);
    }

    @Test
    public void testEvaluationCollisions() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(2)));

        Assert.assertEquals(19, searchResult.getEvaluationCollisions());
    }

    private SearchContext setupContext(SearchContext searchContext) {
        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        for (int i = 0; i < 30; i++) {
            distinctMovesPerLevel[i] = new HashSet<>();
        }

        return searchContext.setVisitedNodesCounters(visitedNodesCounters)
                            .setDistinctMovesPerLevel(distinctMovesPerLevel)
                            .setExpectedNodesCounters(expectedNodesCounters);
    }
}
