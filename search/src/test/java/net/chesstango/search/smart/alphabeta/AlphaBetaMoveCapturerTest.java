package net.chesstango.search.smart.alphabeta;

import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.MoveSorter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Set;

public class AlphaBetaMoveCapturerTest {
    @Test
    public void testCapturer(){
        MoveSorter moveSorter = new MoveSorter();

        QuiescenceNull quiescence = new QuiescenceNull();
        quiescence.setGameEvaluator(new GameEvaluatorByMaterial());

        AlphaBetaMoveCapturer alphaBetaMoveCapturer = new AlphaBetaMoveCapturer();

        AlphaBetaImp alphaBetaImp = new AlphaBetaImp();
        alphaBetaImp.setQuiescence(quiescence);
        alphaBetaImp.setMoveSorter(moveSorter);
        alphaBetaImp.setNext(alphaBetaMoveCapturer);

        alphaBetaMoveCapturer.setNext(alphaBetaImp);

        MinMaxPruning minMaxPruning = new MinMaxPruning();
        minMaxPruning.setAlphaBetaSearch(alphaBetaMoveCapturer);
        minMaxPruning.setMoveSorter(moveSorter);

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMaxPruning.searchBestMove(game, 2);

        List<Set<Move>> distinctMoves = searchResult.getDistinctMoves();

        Assert.assertEquals(20, distinctMoves.get(0).size());
        Assert.assertEquals(20, distinctMoves.get(1).size());
    }
}
