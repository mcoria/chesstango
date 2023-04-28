package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByCondition;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class DetectCycleTest {

    private MinMaxPruning minMaxPruning;

    private GameEvaluatorByCondition evaluator;

    @BeforeEach
    public void setup() {
        evaluator =  new GameEvaluatorByCondition();
        evaluator.setDefaultValue(0);

        // FILTERS START
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(evaluator);

        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescence);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(evaluator);

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        DetectCycle detectCycle = new DetectCycle();
        // FILTERS END

        alphaBeta.setNext(alphaBetaStatistics);
        detectCycle.setNext(alphaBeta);
        alphaBetaStatistics.setNext(detectCycle);

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBeta, alphaBetaStatistics, quiescence, detectCycle));
    }


    /**
     *
     *  -------------------------------
     * 8| k |   |   |   | b |   |   |   |
     *   -------------------------------
     * 7|   |   |   | p | P | p |   |   |
     *   -------------------------------
     * 6|   |   | p | P |   | P |   | p |
     *   -------------------------------
     * 5|   | p | P |   |   |   | p | P |
     *   -------------------------------
     * 4| p | P |   |   |   | p | P |   |
     *   -------------------------------
     * 3| P |   | p |   | p | P |   |   |
     *   -------------------------------
     * 2|   |   | P | p | P |   |   |   |
     *   -------------------------------
     * 1|   |   |   | B |   |   |   | K |
     *   -------------------------------
     *    a   b   c   d   e   f   g   h
     *
     *    Ambos reyes se encuentran atrapados por una muralla de peones trabados. Cada rey puede alcanzar solamente los
     *    siguientes squares
     *    Rey blanco: g1; f1; g2; h2; h3
     *    Rey negro: b8; c8; b7; a7; a6
     *
     *    Por mas profundidad que alcance el algoritmo de busqueda  deberia detectar bucles.
     *
     */

    //TODO: quizas necesitariamos un mapa de posicion->evaluacion
    @Test
    public void detectCycle(){
        Game game = FENDecoder.loadGame("k3b3/3pPp2/2pP1P1p/1pP3pP/pP3pP1/P1p1pP2/2PpP3/3B3K w - - 0 1");

        evaluator.addCondition(theGame -> {
            ChessPositionReader chessPosition = theGame.getChessPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare){
                case g1 -> 1;
                case f1 -> 2;
                case h1 -> 3;
                case h2 -> 4;
                default -> null;
            };
        });


        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, new SearchContext(23));

        assertNotNull(searchResult);
        assertEquals(4, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();

        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        assertEquals(3, visitedNodesCounters[0]);
        assertEquals(5, visitedNodesCounters[1]);
        assertEquals(15, visitedNodesCounters[2]);
        assertEquals(25, visitedNodesCounters[3]);
        assertEquals(58, visitedNodesCounters[4]);
        assertEquals(97, visitedNodesCounters[5]);
        assertEquals(196, visitedNodesCounters[6]);
        assertEquals(259, visitedNodesCounters[7]);
        assertEquals(552, visitedNodesCounters[8]);
        assertEquals(693, visitedNodesCounters[9]);
        assertEquals(1460, visitedNodesCounters[10]);
        assertEquals(1953, visitedNodesCounters[11]);
        assertEquals(4284, visitedNodesCounters[12]);
        assertEquals(5689, visitedNodesCounters[13]);
        assertEquals(11667, visitedNodesCounters[14]);
        assertEquals(16946, visitedNodesCounters[15]);
        assertEquals(34383, visitedNodesCounters[16]);
        assertEquals(47793, visitedNodesCounters[17]);
        assertEquals(96740, visitedNodesCounters[18]);
        assertEquals(157064, visitedNodesCounters[19]);
        assertEquals(314483, visitedNodesCounters[20]);
        assertEquals(468133, visitedNodesCounters[21]);
        assertEquals(941040, visitedNodesCounters[22]);

        assertEquals(2103538, visitedNodesTotal);
    }

}
