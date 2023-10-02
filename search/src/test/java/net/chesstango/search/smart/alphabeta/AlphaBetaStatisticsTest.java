package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.listeners.SetBestMoves;
import net.chesstango.search.smart.alphabeta.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public class AlphaBetaStatisticsTest {

    private AlphaBetaFacade alphaBetaFacade;

    @BeforeEach
    public void setup() {
        MoveSorter moveSorter = new DefaultMoveSorter();

        GameEvaluator gameEvaluator = new EvaluatorByMaterial();

        AlphaBetaStatisticsExpected alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
        AlphaBetaStatisticsVisited alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        QuiescenceNull quiescence = new QuiescenceNull();

        transpositionTable.setNext(alphaBetaStatisticsExpected);

        alphaBetaStatisticsExpected.setNext(alphaBeta);

        alphaBeta.setNext(alphaBetaStatisticsVisited);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);

        alphaBetaFlowControl.setNext(transpositionTable);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        quiescence.setGameEvaluator(gameEvaluator);

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaSearch(alphaBetaStatisticsExpected);
        this.alphaBetaFacade.setSearchActions(List.of(
                new SetTranspositionTables(),  // Lo necesita SetBestMoves
                new SetNodeStatistics(),
                alphaBetaStatisticsExpected,
                alphaBetaStatisticsVisited,
                transpositionTable,
                alphaBetaFlowControl,
                alphaBeta,
                quiescence,
                moveSorter,
                new SetBestMoves()));
    }

    @Test
    @Disabled
    public void testDistinctMoves() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        Set<Move>[] distinctMoves = null; //searchResult.getDistinctMovesPerLevel();

        assertEquals(20, distinctMoves[0].size());
        assertEquals(20, distinctMoves[1].size());
    }

    @Test
    public void test_depth01() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 1);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();
        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();

        assertEquals(20, expectedNodesCounters[0]);
        assertEquals(20, visitedNodesCounters[0]);
    }

    @Test
    public void testVisitedNodesCounters_depth02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);

        /**
         * En el 1er ciclo de exploracion se ejecuta el movimiento de una pieza blanca y el de 20 negras.
         * Dado que la evaluacion es por material y no hay ningun tipo de captura tenemos que alpha = 0
         * Desde el 2do al 20mo ciclo (los otros 19 restantes) se ejecuta un movimiento de pieza blanca y
         * SOLO un movimiento de pieza negra. Cada movimiento de pieza negra produce un beta cutoff debido a que
         * la evaluacion vuelve a ser 0 y no es necesario explorar otros movimientos de pieza negra.
         * Esto se continua repitiendo hasta finalizar los 19 ciclos restantes.
         */
        assertEquals(39, visitedNodesCounters[1]); // ESTA PERFECTO ES 39!!!!
    }

    @Test
    public void testExpectedNodesCounters_depth02() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        int[] expectedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();

        assertEquals(20, expectedNodesCounters[0]);
        assertEquals(400, expectedNodesCounters[1]);
    }

    @Test
    public void testEvaluationCollisions() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        assertEquals(20, searchResult.getBestMovesCounter());
    }
}
