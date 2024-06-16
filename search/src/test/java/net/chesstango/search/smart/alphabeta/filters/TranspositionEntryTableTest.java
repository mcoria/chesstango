package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryTableTest {
    private static final boolean PRINT_REPORT = false;
    private SearchMove searchWithoutTT;
    private SearchMove searchWithTT;
    private SearchMoveResult searchResultWithoutTT;
    private SearchMoveResult searchResultWithTT;

    @BeforeEach
    public void setup() {
        searchWithoutTT = createSearchWithoutTT();
        searchWithTT = createSearchWithTT();
        searchResultWithoutTT = null;
        searchResultWithTT = null;
    }


    @AfterEach
    public void printReport() {
        /*
        if (PRINT_REPORT) {
            new NodesReport()
                    .setReportTitle("Comparation report")
                    .withNodesVisitedStatistics()
                    .withCutoffStatistics()
                    .withMoveResults(Arrays.asList(searchResultWithoutTT, searchResultWithTT))
                    .printReport(System.out);
        }

         */
    }

    @Test
    public void test_01() {
        executeTest(FENDecoder.INITIAL_FEN, 6);
    }

    @Test
    public void test_02() {
        executeTest("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 1", 7);
    }

    @Test
    public void test_03() {
        executeTest("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1", 5);
    }

    @Test
    public void test_04() {
        executeTest("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1", 6);
    }


    public void executeTest(String fen, int depth) {
        Game game01 = FENDecoder.loadGame(fen);
        Game game02 = FENDecoder.loadGame(fen);

        searchWithoutTT.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
        searchResultWithoutTT = searchWithoutTT.search(game01);

        searchWithTT.setSearchParameter(SearchParameter.MAX_DEPTH, depth);
        searchResultWithTT = searchWithTT.search(game02);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString() , searchResult01.getEvaluation(), depth - 1, searchWithoutTT, searchWithTT);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult02.getBestMove()).toString() , searchResult02.getEvaluation(), depth - 1, searchWithTT, searchWithoutTT);

        Assertions.assertEquals(searchResultWithoutTT.getBestEvaluation(), searchResultWithTT.getBestEvaluation());

        Assertions.assertEquals(searchResultWithoutTT.getBestMove(), searchResultWithTT.getBestMove());
    }

    private void debugTT(String fen, int evaluation, int depth, SearchMove searchMethod1, SearchMove searchMethod2) {
        if (depth > 0 && FENDecoder.loadGame(fen).getStatus().isInProgress()) {
            Game game01 = FENDecoder.loadGame(fen);
            Game game02 = FENDecoder.loadGame(fen);

            SearchMoveResult searchResult01 = searchMethod1.search(game01);

            SearchMoveResult searchResult02 = searchMethod2.search(game02);

            Assertions.assertEquals(evaluation, searchResult01.getBestEvaluation());

            debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString(), searchResult01.getBestEvaluation(), depth - 1, searchMethod1, searchMethod2);

            Assertions.assertEquals(searchResult01.getBestEvaluation(), searchResult02.getBestEvaluation());

            Assertions.assertEquals(searchResult01.getBestMove(), searchResult02.getBestMove());
        }
    }


    private SearchMove createSearchWithoutTT() {
        EvaluatorStatisticsWrapper gameEvaluator = new EvaluatorStatisticsWrapper()
                .setImp(new EvaluatorImp04());

        return new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .build();
    }

    private SearchMove createSearchWithTT() {
        EvaluatorStatisticsWrapper gameEvaluator = new EvaluatorStatisticsWrapper()
                .setImp(new EvaluatorImp04());

        return new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .withTranspositionTable()
                .withTranspositionMoveSorter()
                .build();
    }
}
