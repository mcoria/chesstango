package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.listeners.SearchSetup;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private MinMaxPruning minMaxPruning;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new GameEvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setNext(alphaBetaStatistics);
        alphaBeta.setGameEvaluator(gameEvaluator);

        alphaBetaStatistics.setNext(alphaBeta);

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setSearchActions(Arrays.asList(new SearchSetup(), alphaBeta, alphaBetaStatistics, quiescence, moveSorter));
    }

    @Test
    @Disabled
    public void testDistinctMoves() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        minMaxPruning.initSearch(game, 2);
        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(game, 2));
        minMaxPruning.closeSearch(searchResult);

        Set<Move>[] distinctMoves = searchResult.getDistinctMovesPerLevel();

        assertEquals(20, distinctMoves[0].size());
        assertEquals(20, distinctMoves[1].size());
    }

    @Test
    public void testVisitedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        minMaxPruning.initSearch(game, 2);
        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(game, 2));
        minMaxPruning.closeSearch(searchResult);

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(39, visitedNodesCounters[1]); // ESTA PERFECTO ES 39!!!!
    }

    @Test
    public void testExpectedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        minMaxPruning.initSearch(game, 2);
        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(game, 2));
        minMaxPruning.closeSearch(searchResult);

        int[] visitedNodesCounters = searchResult.getExpectedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(400, visitedNodesCounters[1]);
    }

    @Test
    @Disabled // TODO: con el refactoring de MinMaxPruning debemos calcular las colisiones de otra forma
    public void testEvaluationCollisions() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(game, 2));

        assertEquals(19, searchResult.getEvaluationCollisions());
    }
}
