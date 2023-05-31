package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByCondition;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.alphabeta.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaStatistics;
import net.chesstango.search.smart.alphabeta.filters.QuiescenceNull;
import net.chesstango.search.smart.alphabeta.filters.TranspositionTable;
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
public class DetectCycleEnabledTest {

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

        TranspositionTable transpositionTable = new TranspositionTable();
        // FILTERS END

        alphaBeta.setNext(alphaBetaStatistics);
        transpositionTable.setNext(alphaBeta);
        alphaBetaStatistics.setNext(transpositionTable);

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, alphaBetaStatistics, quiescence, transpositionTable, moveSorter));
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
    public void testDetectCycle01() {
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


        SearchMoveResult searchResult = minMaxPruning.search(new SearchContext(23));

        assertNotNull(searchResult);
        assertEquals(4, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        debug(visitedNodesTotal, visitedNodesCounters);


        assertEquals(3, visitedNodesCounters[0]);
        assertEquals(5, visitedNodesCounters[1]);
        assertEquals(15, visitedNodesCounters[2]);
        assertEquals(25, visitedNodesCounters[3]);
        assertEquals(67, visitedNodesCounters[4]);
        assertEquals(130, visitedNodesCounters[5]);
        assertEquals(285, visitedNodesCounters[6]);
        assertEquals(447, visitedNodesCounters[7]);
        assertEquals(998, visitedNodesCounters[8]);
        assertEquals(1526, visitedNodesCounters[9]);
        assertEquals(3402, visitedNodesCounters[10]);
        assertEquals(5271, visitedNodesCounters[11]);
        assertEquals(11591, visitedNodesCounters[12]);
        assertEquals(17107, visitedNodesCounters[13]);
        assertEquals(39413, visitedNodesCounters[14]);
        assertEquals(59492, visitedNodesCounters[15]);
        assertEquals(130017, visitedNodesCounters[16]);
        assertEquals(198746, visitedNodesCounters[17]);
        assertEquals(412216, visitedNodesCounters[18]);
        assertEquals(639181, visitedNodesCounters[19]);
        assertEquals(1210831, visitedNodesCounters[20]);
        assertEquals(2005484, visitedNodesCounters[21]);
        assertEquals(3193116, visitedNodesCounters[22]);
        assertEquals(0, visitedNodesCounters[23]);

        assertEquals(7929368, visitedNodesTotal);
    }


    @Test
    @Disabled
    public void testDetectCycle02() {
        Game game = FENDecoder.loadGame("k2b4/2pPp3/1pP1P3/pP5p/P5pP/3p1pP1/3PpP2/4B2K w - - 0 1");

        evaluator.addCondition(theGame -> {
            ChessPositionReader chessPosition = theGame.getChessPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });


        SearchMoveResult searchResult = new NoIterativeDeepening(minMaxPruning).search(game, 17);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(2, visitedNodesCounters[0]);
        assertEquals(3, visitedNodesCounters[1]);
        assertEquals(5, visitedNodesCounters[2]);
        assertEquals(7, visitedNodesCounters[3]);
        assertEquals(9, visitedNodesCounters[4]);
        assertEquals(16, visitedNodesCounters[5]);
        assertEquals(22, visitedNodesCounters[6]);
        assertEquals(25, visitedNodesCounters[7]);
        assertEquals(43, visitedNodesCounters[8]);
        assertEquals(57, visitedNodesCounters[9]);
        assertEquals(80, visitedNodesCounters[10]);
        assertEquals(106, visitedNodesCounters[11]);
        assertEquals(96, visitedNodesCounters[12]);
        assertEquals(71, visitedNodesCounters[13]);
        assertEquals(66, visitedNodesCounters[14]);
        assertEquals(41, visitedNodesCounters[15]);
        assertEquals(18, visitedNodesCounters[16]);
        assertEquals(0, visitedNodesCounters[17]);

        assertEquals(667, visitedNodesTotal);
    }

    @Test
    @Disabled
    public void testDetectCycle03() {
        Game game = FENDecoder.loadGame("k1p5/1pP5/1p6/1P6/6p1/6P1/5pP1/5P1K w - - 0 1");

        evaluator.addCondition(theGame -> {
            ChessPositionReader chessPosition = theGame.getChessPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });


        SearchMoveResult searchResult = new NoIterativeDeepening(minMaxPruning).search(game, 3);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(1, visitedNodesCounters[0]);
        assertEquals(1, visitedNodesCounters[1]);
        assertEquals(1, visitedNodesCounters[2]);
        assertEquals(0, visitedNodesCounters[3]);

        assertEquals(3, visitedNodesTotal);
    }

    @Test
    @Disabled
    public void testDetectCycle04() {
        Game game = FENDecoder.loadGame("k1p5/1pP5/1p6/1P6/6p1/6P1/5pP1/5P1K w - - 0 1");

        evaluator.addCondition(theGame -> {
            ChessPositionReader chessPosition = theGame.getChessPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });


        //Assertions.assertThrows(CycleException.class, () -> minMaxPruning.search(new SearchContext(game, 4)));
    }

    private void debug(long visitedNodesTotal, int[] visitedNodesCounters) {
        System.out.printf("Total visited Nodes = %d\n", visitedNodesTotal);
        for (int i = 0; i < 30; i++) {
            if(visitedNodesCounters[i] > 0) {
                System.out.printf("Visited Nodes Level %2d = %10d\n", i + 1, visitedNodesCounters[i]);
            }
        }

        for (int i = 0; i < 30; i++) {
            if(visitedNodesCounters[i] > 0) {
                System.out.printf("assertEquals(%d, visitedNodesCounters[%d]);\n", visitedNodesCounters[i], i);
            }
        }
    }
}