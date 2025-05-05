package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.PositionReader;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.evaluation.evaluators.EvaluatorByCondition;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 */
public class DetectCycleEnabledTest {

    private EvaluatorByCondition evaluator;
    private Search search;

    @BeforeEach
    public void setup() {
        evaluator = new EvaluatorByCondition();
        evaluator.setDefaultValue(0);


        this.search = new AlphaBetaBuilder()
                .withGameEvaluator(evaluator)
                .withTranspositionTable()
                .withStatistics()
                .build();
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
    public void testDetectCycle01() {
        Game game = Game.fromFEN("k3b3/3pPp2/2pP1P1p/1pP3pP/pP3pP1/P1p1pP2/2PpP3/3B3K w - - 0 1");

        evaluator.addCondition(theGame -> {
            PositionReader chessPosition = theGame.getPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case f1 -> 2;
                case h1 -> 3;
                case h2 -> 4;
                default -> null;
            };
        });


        search.setSearchParameter(SearchParameter.MAX_DEPTH, 9);
        SearchResult searchResult = search
                .search(game);

        assertNotNull(searchResult);
        assertEquals(3, searchResult.getBestEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        //debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(3, visitedNodesCounters[0]);
        assertEquals(5, visitedNodesCounters[1]);
        assertEquals(15, visitedNodesCounters[2]);
        assertEquals(19, visitedNodesCounters[3]);
        assertEquals(35, visitedNodesCounters[4]);
        assertEquals(45, visitedNodesCounters[5]);
        assertEquals(53, visitedNodesCounters[6]);
        assertEquals(79, visitedNodesCounters[7]);
        assertEquals(54, visitedNodesCounters[8]);
        assertEquals(0, visitedNodesCounters[9]);

        assertEquals(308, visitedNodesTotal);
    }


    @Test
    public void testDetectCycle02() {
        Game game = Game.fromFEN("k2b4/2pPp3/1pP1P3/pP5p/P5pP/3p1pP1/3PpP2/4B2K w - - 0 1");

        evaluator.addCondition(theGame -> {
            PositionReader chessPosition = theGame.getPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 17);
        SearchResult searchResult = search
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getBestEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        //debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(2, visitedNodesCounters[0]);
        assertEquals(3, visitedNodesCounters[1]);
        assertEquals(5, visitedNodesCounters[2]);
        assertEquals(6, visitedNodesCounters[3]);
        assertEquals(7, visitedNodesCounters[4]);
        assertEquals(7, visitedNodesCounters[5]);
        assertEquals(7, visitedNodesCounters[6]);
        assertEquals(3, visitedNodesCounters[7]);
        assertEquals(3, visitedNodesCounters[8]);
        assertEquals(3, visitedNodesCounters[9]);
        assertEquals(3, visitedNodesCounters[10]);
        assertEquals(3, visitedNodesCounters[11]);
        assertEquals(2, visitedNodesCounters[12]);
        assertEquals(0, visitedNodesCounters[13]);

        assertEquals(54, visitedNodesTotal);
    }

    @Test
    public void testDetectCycle03() {
        Game game = Game.fromFEN("k1p5/1pP5/1p6/1P6/6p1/6P1/5pP1/5P1K w - - 0 1");

        evaluator.addCondition(theGame -> {
            PositionReader chessPosition = theGame.getPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 3);
        SearchResult searchResult = search
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getBestEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        //debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(1, visitedNodesCounters[0]);
        assertEquals(1, visitedNodesCounters[1]);
        assertEquals(1, visitedNodesCounters[2]);
        assertEquals(0, visitedNodesCounters[3]);

        assertEquals(3, visitedNodesTotal);
    }

    @Test
    public void testDetectCycle04() {
        Game game = Game.fromFEN("k1p5/1pP5/1p6/1P6/6p1/6P1/5pP1/5P1K w - - 0 1");

        evaluator.addCondition(theGame -> {
            PositionReader chessPosition = theGame.getPosition();
            Square kingSquare = chessPosition.getKingSquare(Color.WHITE);
            return switch (kingSquare) {
                case g1 -> 1;
                case h2 -> 2;
                default -> null;
            };
        });

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 4);
        SearchResult searchResult = search
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getBestEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        //debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(1, visitedNodesCounters[0]);
        assertEquals(1, visitedNodesCounters[1]);
        assertEquals(1, visitedNodesCounters[2]);
        assertEquals(1, visitedNodesCounters[3]); // Esta posicion se repite

        assertEquals(4, visitedNodesTotal);
    }

    private void debug(long visitedNodesTotal, int[] visitedNodesCounters) {
        System.out.printf("Total visited Nodes = %d\n", visitedNodesTotal);
        for (int i = 0; i < 30; i++) {
            if (visitedNodesCounters[i] > 0) {
                System.out.printf("Visited Nodes Level %2d = %10d\n", i + 1, visitedNodesCounters[i]);
            }
        }

        for (int i = 0; i < 30; i++) {
            if (visitedNodesCounters[i] > 0) {
                System.out.printf("assertEquals(%d, visitedNodesCounters[%d]);\n", visitedNodesCounters[i], i);
            }
        }
    }
}
