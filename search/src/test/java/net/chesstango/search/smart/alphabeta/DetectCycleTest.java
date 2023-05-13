package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByCondition;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.filters.DetectCycle;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
        evaluator = new GameEvaluatorByCondition();
        evaluator.setDefaultValue(0);

        // FILTERS START
        MoveSorter moveSorter = new DefaultMoveSorter();

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
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, alphaBetaStatistics, quiescence, detectCycle));
    }


    /**
     * -------------------------------
     * 8| k |   |   |   | b |   |   |   |
     * -------------------------------
     * 7|   |   |   | p | P | p |   |   |
     * -------------------------------
     * 6|   |   | p | P |   | P |   | p |
     * -------------------------------
     * 5|   | p | P |   |   |   | p | P |
     * -------------------------------
     * 4| p | P |   |   |   | p | P |   |
     * -------------------------------
     * 3| P |   | p |   | p | P |   |   |
     * -------------------------------
     * 2|   |   | P | p | P |   |   |   |
     * -------------------------------
     * 1|   |   |   | B |   |   |   | K |
     * -------------------------------
     * a   b   c   d   e   f   g   h
     * <p>
     * Ambos reyes se encuentran atrapados por una muralla de peones trabados. Cada rey puede alcanzar solamente los
     * siguientes squares
     * Rey blanco: g1; f1; g2; h2; h3
     * Rey negro: b8; c8; b7; a7; a6
     * <p>
     * Por mas profundidad que alcance el algoritmo de busqueda  deberia detectar bucles.
     */

    //TODO: quizas necesitariamos un mapa de posicion->evaluacion
    @Test
    @Disabled
    public void detectCycle() {
        Game game = FENDecoder.loadGame("k3b3/3pPp2/2pP1P1p/1pP3pP/pP3pP1/P1p1pP2/2PpP3/3B3K w - - 0 1");

        evaluator.addCondition(theGame -> {
            ChessPositionReader chessPosition = theGame.getChessPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case f1 -> 2;
                case h1 -> 3;
                case h2 -> 4;
                default -> null;
            };
        });


        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(game, 23));

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
        assertEquals(268, visitedNodesCounters[7]);
        assertEquals(561, visitedNodesCounters[8]);
        assertEquals(714, visitedNodesCounters[9]);
        assertEquals(1511, visitedNodesCounters[10]);
        assertEquals(2009, visitedNodesCounters[11]);
        assertEquals(3966, visitedNodesCounters[12]);
        assertEquals(4577, visitedNodesCounters[13]);
        assertEquals(9587, visitedNodesCounters[14]);
        assertEquals(10887, visitedNodesCounters[15]);
        assertEquals(21522, visitedNodesCounters[16]);
        assertEquals(23531, visitedNodesCounters[17]);
        assertEquals(45789, visitedNodesCounters[18]);
        assertEquals(45769, visitedNodesCounters[19]);
        assertEquals(88661, visitedNodesCounters[20]);
        assertEquals(96905, visitedNodesCounters[21]);
        assertEquals(200949, visitedNodesCounters[22]);
        assertEquals(0, visitedNodesCounters[23]);

        assertEquals(557605, visitedNodesTotal);
    }

}
