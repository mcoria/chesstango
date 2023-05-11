package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Mauricio Coria
 */
public class TranspositionTableTest {

    private ExecutorService executor;

    private SearchMove searchWithoutTT;

    private SearchMove searchWithTT;

    @BeforeEach
    public void setup(){
        executor = Executors.newFixedThreadPool(2);

        searchWithoutTT = createSearchWithoutTT();
        searchWithTT = createSearchWithTT();
    }

    @Test
    public void test_01() throws ExecutionException, InterruptedException {
        Game game01 = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        Game game02 = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Future<SearchMoveResult> searchTask01 =  executor.submit(() -> searchWithoutTT.searchBestMove(game01, 7));
        Future<SearchMoveResult> searchTask02 =  executor.submit(() -> searchWithTT.searchBestMove(game02, 7));
        

        while ( !searchTask01.isDone()  || !searchTask02.isDone() ) {
            Thread.sleep(1000);
        };

        SearchMoveResult searchResult01 = searchTask01.get();
        SearchMoveResult searchResult02 = searchTask02.get();

        Assertions.assertEquals(searchResult01.getEvaluation(), searchResult02.getEvaluation());
    }


    private SearchMove createSearchWithoutTT() {
        GameEvaluator gameEvaluator = new GameEvaluatorSEandImp02();

        MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);


        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setNext(alphaBeta);
        alphaBeta.setQuiescence(quiescenceNull);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(gameEvaluator);


        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescenceNull, moveSorter));
        minMaxPruning.setAlphaBetaSearch(alphaBeta);

        return new NoIterativeDeepening(minMaxPruning);
    }

    private SearchMove createSearchWithTT() {
        GameEvaluator gameEvaluator = new GameEvaluatorSEandImp02();

        MoveSorter moveSorter = new DefaultMoveSorter();

        QuiescenceNull quiescenceNull = new QuiescenceNull();
        quiescenceNull.setGameEvaluator(gameEvaluator);


        AlphaBeta alphaBeta = new AlphaBeta();
        alphaBeta.setQuiescence(quiescenceNull);
        alphaBeta.setMoveSorter(moveSorter);
        alphaBeta.setGameEvaluator(gameEvaluator);

        TranspositionTable transpositionTable = new TranspositionTable();
        transpositionTable.setNext(alphaBeta);

        alphaBeta.setNext(transpositionTable);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setSearchActions(Arrays.asList(alphaBeta, quiescenceNull, moveSorter));
        minMaxPruning.setAlphaBetaSearch(alphaBeta);

        return new NoIterativeDeepening(minMaxPruning);
    }
}
