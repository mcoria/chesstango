package net.chesstango.search.smart.alphabeta.filters;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorSEandImp02;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.IterativeDeepening;
import net.chesstango.search.smart.NoIterativeDeepening;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import net.chesstango.search.smart.sorters.DefaultMoveSorter;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.sorters.TranspositionMoveSorter;
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

    @BeforeEach
    public void setup() {
        searchWithoutTT = createSearchWithoutTT();
        searchWithTT = createSearchWithTT();
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
        executeTest("r3r1k1/pp1n1ppp/2p5/4Pb2/2B2P2/B1P5/P5PP/R2R2K1 w - -", 6);
    }


    public void executeTest(String fen, int depth) {
        Game game = FENDecoder.loadGame(fen);

        SearchMoveResult searchResult01 = searchWithTT.searchBestMove(game, depth);

        game.executeMove(searchResult01.getBestMove());

        SearchMoveResult searchResult02 = searchWithoutTT.searchBestMove(game, depth - 1);

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

        MoveSorter moveSorter = new TranspositionMoveSorter();

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

        return new IterativeDeepening(minMaxPruning);
    }
}
