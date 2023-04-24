package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.ChessPositionReader;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByCondition;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import net.chesstango.search.smart.SearchContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

/**
 * @author Mauricio Coria
 */
public class DetectCycleTest {

    private MinMaxPruning minMaxPruning;

    private GameEvaluatorByCondition evaluator;

    @Before
    public void setup() {
        evaluator =  new GameEvaluatorByCondition();
        evaluator.setDefaultValue(0);

        // FILTERS START
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(evaluator);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);

        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        DetectCycle detectCycle = new DetectCycle();
        // FILTERS END

        alphaBetaImp.setNext(alphaBetaStatistics);
        detectCycle.setNext(alphaBetaImp);
        alphaBetaStatistics.setNext(detectCycle);

        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);
        minMaxPruning.setMoveSorter(moveSorter);
        minMaxPruning.setFilters(Arrays.asList(alphaBetaImp, alphaBetaStatistics, quiescence, detectCycle));
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


        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, setupContext(new SearchContext(23)));

        Assert.assertNotNull(searchResult);
        Assert.assertEquals(4, searchResult.getEvaluation());

        int[] visitedNodesCounters = searchResult.getVisitedNodesCounters();

        long visitedNodesTotal = IntStream.range(0, 30).map(i -> visitedNodesCounters[i]).sum();

        Assert.assertEquals(3, visitedNodesCounters[0]);
        Assert.assertEquals(5, visitedNodesCounters[1]);
        Assert.assertEquals(15, visitedNodesCounters[2]);
        Assert.assertEquals(25, visitedNodesCounters[3]);
        Assert.assertEquals(58, visitedNodesCounters[4]);
        Assert.assertEquals(97, visitedNodesCounters[5]);
        Assert.assertEquals(196, visitedNodesCounters[6]);
        Assert.assertEquals(259, visitedNodesCounters[7]);
        Assert.assertEquals(552, visitedNodesCounters[8]);
        Assert.assertEquals(693, visitedNodesCounters[9]);
        Assert.assertEquals(1460, visitedNodesCounters[10]);
        Assert.assertEquals(1953, visitedNodesCounters[11]);
        Assert.assertEquals(4284, visitedNodesCounters[12]);
        Assert.assertEquals(5689, visitedNodesCounters[13]);
        Assert.assertEquals(11667, visitedNodesCounters[14]);
        Assert.assertEquals(16946, visitedNodesCounters[15]);
        Assert.assertEquals(34383, visitedNodesCounters[16]);
        Assert.assertEquals(47793, visitedNodesCounters[17]);
        Assert.assertEquals(96740, visitedNodesCounters[18]);
        Assert.assertEquals(157064, visitedNodesCounters[19]);
        Assert.assertEquals(314483, visitedNodesCounters[20]);
        Assert.assertEquals(468133, visitedNodesCounters[21]);
        Assert.assertEquals(941040, visitedNodesCounters[22]);

        Assert.assertEquals(2103538, visitedNodesTotal);
    }

    private SearchContext setupContext(SearchContext searchContext) {
        int[] visitedNodesCounters = new int[30];
        int[] expectedNodesCounters = new int[30];
        Set<Move>[] distinctMovesPerLevel = new Set[30];
        for (int i = 0; i < 30; i++) {
            distinctMovesPerLevel[i] = new HashSet<>();
        }
        return searchContext.setVisitedNodesCounters(visitedNodesCounters)
                .setDistinctMovesPerLevel(distinctMovesPerLevel)
                .setExpectedNodesCounters(expectedNodesCounters);
    }
}
