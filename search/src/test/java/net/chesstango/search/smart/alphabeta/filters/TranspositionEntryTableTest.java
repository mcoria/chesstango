package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorSEandImp02;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.SearchParameter;
import net.chesstango.search.reports.NodesReport;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import net.chesstango.search.smart.alphabeta.listeners.SetNodeStatistics;
import net.chesstango.search.smart.alphabeta.listeners.SetPrincipalVariation;
import net.chesstango.search.smart.alphabeta.listeners.SetTranspositionTables;
import net.chesstango.search.smart.alphabeta.listeners.SetupGameEvaluator;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        if (PRINT_REPORT) {
            new NodesReport()
                    .setReportTitle("Comparation report")
                    .withNodesVisitedStatistics()
                    .withCutoffStatistics()
                    .withMoveResults(Arrays.asList(searchResultWithoutTT, searchResultWithTT))
                    .printReport(System.out);
        }
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

        searchWithoutTT.setParameter(SearchParameter.MAX_DEPTH, depth);
        searchResultWithoutTT = searchWithoutTT.search(game01);

        searchWithTT.setParameter(SearchParameter.MAX_DEPTH, depth);
        searchResultWithTT = searchWithTT.search(game02);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString() , searchResult01.getEvaluation(), depth - 1, searchWithoutTT, searchWithTT);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult02.getBestMove()).toString() , searchResult02.getEvaluation(), depth - 1, searchWithTT, searchWithoutTT);

        Assertions.assertEquals(searchResultWithoutTT.getEvaluation(), searchResultWithTT.getEvaluation());

        Assertions.assertEquals(searchResultWithoutTT.getBestMove(), searchResultWithTT.getBestMove());
    }

    private void debugTT(String fen, int evaluation, int depth, SearchMove searchMethod1, SearchMove searchMethod2) {
        if (depth > 0 && FENDecoder.loadGame(fen).getStatus().isInProgress()) {
            Game game01 = FENDecoder.loadGame(fen);
            Game game02 = FENDecoder.loadGame(fen);

            SearchMoveResult searchResult01 = searchMethod1.search(game01);

            SearchMoveResult searchResult02 = searchMethod2.search(game02);

            Assertions.assertEquals(evaluation, searchResult01.getEvaluation());

            debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString(), searchResult01.getEvaluation(), depth - 1, searchMethod1, searchMethod2);

            Assertions.assertEquals(searchResult01.getEvaluation(), searchResult02.getEvaluation());

            Assertions.assertEquals(searchResult01.getBestMove(), searchResult02.getBestMove());
        }
    }


    private SearchMove createSearchWithoutTT() {
        EvaluatorStatistics gameEvaluator = new EvaluatorStatistics(new EvaluatorSEandImp02());

        MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);

        AlphaBeta alphaBeta = new AlphaBeta();
        AlphaBetaStatisticsExpected alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
        AlphaBetaStatisticsVisited alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        SetupGameEvaluator setupGameEvaluator = new SetupGameEvaluator();

        alphaBetaStatisticsExpected.setNext(alphaBeta);

        alphaBeta.setNext(alphaBetaStatisticsVisited);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);

        alphaBetaFlowControl.setNext(alphaBetaStatisticsExpected);
        alphaBetaFlowControl.setQuiescence(quiescenceNull);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        AlphaBetaFacade minMaxPruning = new AlphaBetaFacade();
        minMaxPruning.setSearchActions(Arrays.asList(
                new SetTranspositionTables(),
                new SetNodeStatistics(),
                alphaBeta,
                alphaBetaStatisticsExpected,
                alphaBetaStatisticsVisited,
                quiescenceNull,
                moveSorter,
                gameEvaluator,
                alphaBetaFlowControl,
                setupGameEvaluator));
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatisticsExpected);

        return new NoIterativeDeepening(minMaxPruning);
    }

    private SearchMove createSearchWithTT() {
        EvaluatorStatistics gameEvaluator = new EvaluatorStatistics(new EvaluatorSEandImp02());

        MoveSorter moveSorter = new TranspositionMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);

        AlphaBeta alphaBeta = new AlphaBeta();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBetaStatisticsExpected alphaBetaStatisticsExpected = new AlphaBetaStatisticsExpected();
        AlphaBetaStatisticsVisited alphaBetaStatisticsVisited = new AlphaBetaStatisticsVisited();
        AlphaBetaFlowControl alphaBetaFlowControl = new AlphaBetaFlowControl();
        SetupGameEvaluator setupGameEvaluator = new SetupGameEvaluator();

        transpositionTable.setNext(alphaBetaStatisticsExpected);

        alphaBetaStatisticsExpected.setNext(alphaBeta);

        alphaBeta.setNext(alphaBetaStatisticsVisited);
        alphaBeta.setMoveSorter(moveSorter);

        alphaBetaStatisticsVisited.setNext(alphaBetaFlowControl);

        alphaBetaFlowControl.setNext(transpositionTable);
        alphaBetaFlowControl.setQuiescence(quiescenceNull);
        alphaBetaFlowControl.setGameEvaluator(gameEvaluator);

        setupGameEvaluator.setGameEvaluator(gameEvaluator);

        AlphaBetaFacade minMaxPruning = new AlphaBetaFacade();
        minMaxPruning.setSearchActions(Arrays.asList(
                new SetTranspositionTables(),
                new SetNodeStatistics(),
                alphaBeta,
                transpositionTable,
                alphaBetaStatisticsExpected,
                alphaBetaStatisticsVisited,
                quiescenceNull,
                moveSorter,
                gameEvaluator,
                new SetPrincipalVariation(),
                alphaBetaFlowControl,
                setupGameEvaluator));
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatisticsExpected);

        return new IterativeDeepening(minMaxPruning);
    }
}
