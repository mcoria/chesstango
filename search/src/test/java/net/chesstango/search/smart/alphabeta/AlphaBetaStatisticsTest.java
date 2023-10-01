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

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        QuiescenceNull quiescence = new QuiescenceNull();

        alphaBetaStatistics.setNext(transpositionTable);

        transpositionTable.setNext(alphaBetaFlowControl);

        alphaBetaFlowControl.setNext(alphaBeta);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        alphaBeta.setNext(alphaBetaStatistics);
        alphaBeta.setMoveSorter(moveSorter);

        quiescence.setGameEvaluator(gameEvaluator);

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaSearch(alphaBetaStatistics);
        this.alphaBetaFacade.setSearchActions(List.of(
                new SetTranspositionTables(),
                alphaBetaStatistics,
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
    public void testVisitedNodesCounters() {
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
    public void testExpectedNodesCounters() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().expectedNodesCounters();

        assertEquals(20, visitedNodesCounters[0]);
        assertEquals(400, visitedNodesCounters[1]);
    }

    @Test
    public void testEvaluationCollisions() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = new NoIterativeDeepening(alphaBetaFacade).search(game, 2);

        assertEquals(20, searchResult.getBestMovesCounter());
    }
}
