package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.evaluation.evaluators.EvaluatorImp04;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.smart.features.statistics.evaluation.EvaluatorStatisticsWrapper;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Mauricio Coria
 */
public class TranspositionEntryTableTest {
    private static final boolean PRINT_REPORT = false;
    private Search searchWithoutTT;
    private Search searchWithTT;
    private SearchResult searchResultWithoutTT;
    private SearchResult searchResultWithTT;

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
        executeTest(FENParser.INITIAL_FEN, 6);
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
        Game game01 = Game.from(FEN.of(fen));
        Game game02 = Game.from(FEN.of(fen));

        searchWithoutTT.accept(new SetMaxDepthVisitor(depth));
        searchResultWithoutTT = searchWithoutTT.startSearch(game01);

        searchWithTT.accept(new SetMaxDepthVisitor(depth));
        searchResultWithTT = searchWithTT.startSearch(game02);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString() , searchResult01.getEvaluation(), depth - 1, searchWithoutTT, searchWithTT);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult02.getBestMove()).toString() , searchResult02.getEvaluation(), depth - 1, searchWithTT, searchWithoutTT);

        Assertions.assertEquals(searchResultWithoutTT.getBestEvaluation(), searchResultWithTT.getBestEvaluation());

        Assertions.assertEquals(searchResultWithoutTT.getBestMove(), searchResultWithTT.getBestMove());
    }

    private void debugTT(String fen, int evaluation, int depth, Search searchMethod1, Search searchMethod2) {
        if (depth > 0 && Game.from(FEN.of(fen)).getStatus().isInProgress()) {
            Game game01 = Game.from(FEN.of(fen));
            Game game02 = Game.from(FEN.of(fen));

            SearchResult searchResult01 = searchMethod1.startSearch(game01);

            SearchResult searchResult02 = searchMethod2.startSearch(game02);

            Assertions.assertEquals(evaluation, searchResult01.getBestEvaluation());

            Move bestMove = searchResult01.getBestMove();

            debugTT(Game.from(FEN.of(fen)).executeMove(bestMove.getFrom().square(), bestMove.getTo().square()).toString(), searchResult01.getBestEvaluation(), depth - 1, searchMethod1, searchMethod2);

            Assertions.assertEquals(searchResult01.getBestEvaluation(), searchResult02.getBestEvaluation());

            Assertions.assertEquals(searchResult01.getBestMove(), searchResult02.getBestMove());
        }
    }


    private Search createSearchWithoutTT() {
        EvaluatorStatisticsWrapper gameEvaluator = new EvaluatorStatisticsWrapper()
                .setImp(new EvaluatorImp04());

        return new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .build();
    }

    private Search createSearchWithTT() {
        EvaluatorStatisticsWrapper gameEvaluator = new EvaluatorStatisticsWrapper()
                .setImp(new EvaluatorImp04());

        return new AlphaBetaBuilder()
                .withGameEvaluator(gameEvaluator)
                .withTranspositionTable()
                .withTranspositionMoveSorter()
                .build();
    }
}
