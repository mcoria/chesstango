package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaImp;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.filters.TranspositionTable;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoves;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
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

    private AlphaBeta alphaBeta;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(gameEvaluator);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();


        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaStatistics);
        alphaBetaImp.setGameEvaluator(gameEvaluator);

        transpositionTable.setNext(alphaBetaImp);

        alphaBetaStatistics.setNext(transpositionTable);

        this.alphaBeta = new AlphaBeta();
        this.alphaBeta.setAlphaBetaSearch(alphaBetaStatistics);
        this.alphaBeta.setSearchActions(Arrays.asList(new SetTranspositionTables(),
                alphaBetaImp,
                alphaBetaStatistics,
                transpositionTable,
                quiescence,
                moveSorter,
                new SetBestMoves()));
    }

    @Test
    @Disabled
    public void testDistinctMoves() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game, 2);

        Set<Move>[] distinctMoves = null; //searchResult.getDistinctMovesPerLevel();

        assertEquals(20, distinctMoves[0].size());
        assertEquals(20, distinctMoves[1].size());
    }

    @Test
    public void testVisitedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(39, visitedNodesCounters[1]); // ESTA PERFECTO ES 39!!!!
    }

    @Test
    public void testExpectedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(400, visitedNodesCounters[1]);
    }

    @Test
    public void testEvaluationCollisions() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBeta).search(game, 2);

        assertEquals(19, searchResult.getEvaluationCollisions());
    }
}
