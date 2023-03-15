package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class AlphaBetaStatisticsTest {
    @Test
    public void testCapturer() {
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaStatistics);

        alphaBetaStatistics.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, alphaBetaStatistics, quiescence));

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(2)));

        List<Set<Move>> distinctMoves = searchResult.getDistinctMovesPerLevel();

        Assert.assertEquals(20, distinctMoves.get(0).size());
        Assert.assertEquals(20, distinctMoves.get(1).size());
    }

    private SearchContext setupContext(SearchContext searchContext) {
        int[] visitedNodesCounters = new int[30];
        List<Set<Move>> distinctMovesPerLevel = new ArrayList<>(visitedNodesCounters.length);
        for (int i = 0; i < 30; i++) {
            distinctMovesPerLevel.add(new HashSet<>());
        }
        return searchContext.setVisitedNodesCounters(visitedNodesCounters)
                            .setDistinctMovesPerLevel(distinctMovesPerLevel);
    }
}
