package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByFEN;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class SearchTest {


    @Test
    @Ignore //TODO: resolver
    public void testSearch(){
        GameEvaluatorByFEN evaluatorMock =  new GameEvaluatorByFEN();
        evaluatorMock.setDefaultValue(0);
        evaluatorMock.addEvaluation("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", 1);

        MinMax minMax = new MinMax();
        minMax.setGameEvaluator(evaluatorMock);

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = minMax.searchBestMove(game, new SearchContext(1));
        Move bestMove = searchResult.getBestMove();
        Assert.assertEquals(Square.e2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.e4, bestMove.getTo().getSquare());
        Assert.assertEquals(1, searchResult.getEvaluation());

        /**
         * Si bien cualquier movimiento posible es optimo, no pasamos por el maximo de forma temprana
         */
        searchResult = minMax.searchBestMove(game, new SearchContext(3));
        bestMove = searchResult.getBestMove();
        Assert.assertEquals(Square.e2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.e4, bestMove.getTo().getSquare());
        Assert.assertEquals(1, searchResult.getEvaluation());
    }
}
