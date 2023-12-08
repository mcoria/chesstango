package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByCondition;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.SmartListenerMediator;
import net.chesstango.search.smart.alphabeta.filters.*;
import net.chesstango.search.smart.alphabeta.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.listeners.SetupGameEvaluator;
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
public class DetectCycleDisabledTest {

    private AlphaBetaFacade alphaBetaFacade;

    private SmartListenerMediator smartListenerMediator;
    private EvaluatorByCondition evaluator;

    @BeforeEach
    public void setup() {
        evaluator = new EvaluatorByCondition();
        evaluator.setDefaultValue(0);

        // FILTERS START
        MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(evaluator);

        AlphaBetaStatisticsExpected alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
        AlphaBetaStatisticsVisited alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaFlowControl alphaBetaFlowControl =  new AlphaBetaFlowControl();
        SetupGameEvaluator setupGameEvaluator = new SetupGameEvaluator();

        alphaBetaStatisticsExpected.setNext(alphaBeta);

        alphaBeta.setNext(alphaBetaStatisticsVisited);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);

        alphaBetaFlowControl.setNext(alphaBetaStatisticsExpected);
        alphaBetaFlowControl.setQuiescence(quiescence);
        alphaBetaFlowControl.setGameEvaluator(evaluator);

        setupGameEvaluator.setGameEvaluator(evaluator);

        this.smartListenerMediator = new SmartListenerMediator();

        this.alphaBetaFacade = new AlphaBetaFacade();
        this.alphaBetaFacade.setAlphaBetaFilter(alphaBetaStatisticsExpected);
        this.alphaBetaFacade.setSmartListenerMediator(smartListenerMediator);

        this.smartListenerMediator.addAll(Arrays.asList(
                new SetNodeStatistics(),
                alphaBeta,
                alphaBetaStatisticsExpected,
                alphaBetaStatisticsVisited,
                quiescence,
                moveSorter,
                alphaBetaFlowControl,
                setupGameEvaluator,
                alphaBetaFacade));
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


        NoIterativeDeepening searchMove = new NoIterativeDeepening(alphaBetaFacade);

        searchMove.setParameter(SearchParameter.MAX_DEPTH, 23);
        SearchMoveResult searchResult = searchMove
                .search(game);

        assertNotNull(searchResult);
        assertEquals(4, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(3, visitedNodesCounters[0]);
        assertEquals(5, visitedNodesCounters[1]);
        assertEquals(15, visitedNodesCounters[2]);
        assertEquals(25, visitedNodesCounters[3]);
        assertEquals(55, visitedNodesCounters[4]);
        assertEquals(85, visitedNodesCounters[5]);
        assertEquals(203, visitedNodesCounters[6]);
        assertEquals(321, visitedNodesCounters[7]);
        assertEquals(727, visitedNodesCounters[8]);
        assertEquals(1133, visitedNodesCounters[9]);
        assertEquals(2623, visitedNodesCounters[10]);
        assertEquals(4113, visitedNodesCounters[11]);
        assertEquals(9411, visitedNodesCounters[12]);
        assertEquals(14709, visitedNodesCounters[13]);
        assertEquals(33839, visitedNodesCounters[14]);
        assertEquals(52969, visitedNodesCounters[15]);
        assertEquals(121527, visitedNodesCounters[16]);
        assertEquals(190085, visitedNodesCounters[17]);
        assertEquals(436683, visitedNodesCounters[18]);
        assertEquals(683281, visitedNodesCounters[19]);
        assertEquals(1910335, visitedNodesCounters[20]);
        assertEquals(3681163, visitedNodesCounters[21]);
        assertEquals(10885823, visitedNodesCounters[22]);
        assertEquals(0, visitedNodesCounters[23]);

        assertEquals(18029133, visitedNodesTotal);
    }


    @Test
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


        NoIterativeDeepening searchMove = new NoIterativeDeepening(alphaBetaFacade);
        searchMove.setSmartListenerMediator(smartListenerMediator);

        searchMove.setParameter(SearchParameter.MAX_DEPTH, 17);
        SearchMoveResult searchResult = searchMove
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getRegularNodeStatistics().visitedNodesCounters();
        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        //debug(visitedNodesTotal, visitedNodesCounters);

        assertEquals(2, visitedNodesCounters[0]);
        assertEquals(3, visitedNodesCounters[1]);
        assertEquals(5, visitedNodesCounters[2]);
        assertEquals(7, visitedNodesCounters[3]);
        assertEquals(11, visitedNodesCounters[4]);
        assertEquals(15, visitedNodesCounters[5]);
        assertEquals(23, visitedNodesCounters[6]);
        assertEquals(33, visitedNodesCounters[7]);
        assertEquals(47, visitedNodesCounters[8]);
        assertEquals(73, visitedNodesCounters[9]);
        assertEquals(100, visitedNodesCounters[10]);
        assertEquals(139, visitedNodesCounters[11]);
        assertEquals(188, visitedNodesCounters[12]);
        assertEquals(292, visitedNodesCounters[13]);
        assertEquals(401, visitedNodesCounters[14]);
        assertEquals(609, visitedNodesCounters[15]);
        assertEquals(694, visitedNodesCounters[16]);
        assertEquals(0, visitedNodesCounters[17]);


        assertEquals(2642, visitedNodesTotal);
    }

    @Test
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


        NoIterativeDeepening searchMove = new NoIterativeDeepening(alphaBetaFacade);
        searchMove.setSmartListenerMediator(smartListenerMediator);

        searchMove.setParameter(SearchParameter.MAX_DEPTH, 3);
        SearchMoveResult searchResult = searchMove
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getEvaluation());

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

        NoIterativeDeepening searchMove = new NoIterativeDeepening(alphaBetaFacade);
        searchMove.setSmartListenerMediator(smartListenerMediator);

        searchMove.setParameter(SearchParameter.MAX_DEPTH, 4);
        SearchMoveResult searchResult = searchMove
                .search(game);

        assertNotNull(searchResult);
        assertEquals(0, searchResult.getEvaluation());

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
