package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.reports.SearchesReport;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.AlphaBeta;
import net.chesstango.search.smart.alphabeta.listeners.SearchSetup;
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
public class TranspositionTableTest {

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
    public void printReport(){
        new SearchesReport()
                .withNodesVisitedStatics()
                .withCutoffStatics()
                .printSearchesStatics(Arrays.asList(searchResultWithoutTT, searchResultWithTT));
    }

    @Test
    public void test_01(){
        executeTest(FENDecoder.INITIAL_FEN, 6);
    }

    @Test
    public void test_02(){
        executeTest("1k1r4/pp1b1R2/3q2pp/4p3/2B5/4Q3/PPP2B2/2K5 b - - 0 1", 7);
    }

    @Test
    public void test_03()  {
        executeTest("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1", 5);
    }

    @Test
    public void test_04()  {
        executeTest("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - - 0 1", 6);
    }


    public void executeTest(String fen, int depth) {
        Game game01 = FENDecoder.loadGame(fen);
        Game game02 = FENDecoder.loadGame(fen);


        searchResultWithoutTT = searchWithoutTT.search(game01, depth);

        searchResultWithTT = searchWithTT.search(game02, depth);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString() , searchResult01.getEvaluation(), depth - 1, searchWithoutTT, searchWithTT);

        //debugTT(FENDecoder.loadGame(fen).executeMove(searchResult02.getBestMove()).toString() , searchResult02.getEvaluation(), depth - 1, searchWithTT, searchWithoutTT);

        Assertions.assertEquals(searchResultWithoutTT.getEvaluation(), searchResultWithTT.getEvaluation());

        Assertions.assertEquals(searchResultWithoutTT.getBestMove(), searchResultWithTT.getBestMove());
    }

    private void debugTT(String fen, int evaluation, int depth, SearchMove searchMethod1, SearchMove searchMethod2) {
        if(depth > 0 && FENDecoder.loadGame(fen).getStatus().isInProgress()) {
            Game game01 = FENDecoder.loadGame(fen);
            Game game02 = FENDecoder.loadGame(fen);

            SearchMoveResult searchResult01 = searchMethod1.search(game01, depth);

            SearchMoveResult searchResult02 = searchMethod2.search(game02, depth);

            Assertions.assertEquals(evaluation, searchResult01.getEvaluation());

            debugTT(FENDecoder.loadGame(fen).executeMove(searchResult01.getBestMove()).toString(), searchResult01.getEvaluation(), depth - 1, searchMethod1, searchMethod2);

            Assertions.assertEquals(searchResult01.getEvaluation(), searchResult02.getEvaluation());

            Assertions.assertEquals(searchResult01.getBestMove(), searchResult02.getBestMove());
        }
    }


    private SearchMove createSearchWithoutTT() {
        GameEvaluator gameEvaluator = new GameEvaluatorSEandImp02();

        MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        alphaBetaImp.setNext(alphaBetaStatistics);
        alphaBetaImp.setQuiescence(quiescenceNull);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setGameEvaluator(gameEvaluator);

        alphaBetaStatistics.setNext(alphaBetaImp);;

        AlphaBeta minMaxPruning = new AlphaBeta();
        minMaxPruning.setSearchActions(Arrays.asList(new SearchSetup(), alphaBetaImp, alphaBetaStatistics, quiescenceNull, moveSorter));
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);

        return new NoIterativeDeepening(minMaxPruning);
    }

    private SearchMove createSearchWithTT() {
        GameEvaluator gameEvaluator = new GameEvaluatorSEandImp02();

        MoveSorter moveSorter = new TranspositionMoveSorter();
        //MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        TranspositionTable transpositionTable = new TranspositionTable();
        AlphaBetaStatistics alphaBetaStatistics = new AlphaBetaStatistics();

        alphaBetaImp.setNext(alphaBetaStatistics);
        alphaBetaImp.setQuiescence(quiescenceNull);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setGameEvaluator(gameEvaluator);

        transpositionTable.setNext(alphaBetaImp);

        alphaBetaStatistics.setNext(transpositionTable);


        AlphaBeta minMaxPruning = new AlphaBeta();
        minMaxPruning.setSearchActions(Arrays.asList(new SearchSetup(), alphaBetaImp, transpositionTable, alphaBetaStatistics, quiescenceNull, moveSorter));
        minMaxPruning.setAlphaBetaSearch(alphaBetaStatistics);

        return new IterativeDeepening(minMaxPruning);
    }
}
