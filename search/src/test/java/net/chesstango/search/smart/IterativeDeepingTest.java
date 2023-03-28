package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByFEN;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.minmax.MinMax;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepingTest {

    private IterativeDeeping iterativeDeeping;

    private MinMax smart;

    @Before
    public void setup(){
        smart = new MinMax();

        iterativeDeeping = new IterativeDeeping(smart);
    }

    @Test
    public void testSearch(){
        GameEvaluatorByFEN evaluatorMock =  new GameEvaluatorByFEN();
        evaluatorMock.setDefaultValue(0);
        evaluatorMock.addEvaluation("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", 1);

        smart.setGameEvaluator(evaluatorMock);

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = iterativeDeeping.searchBestMove(game, 1);
        Move bestMove = searchResult.getBestMove();
        Assert.assertEquals(Square.e2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth 1 el movimiento e2e4 es evaluado en 1
        Assert.assertEquals(1, searchResult.getEvaluation());

        /**
         * Repetimos la busqueda en depth = 3, acá la evaluacion de todos los movimientos es la misma.
         * Lo que queremos es priorizar aquellos movimientos que tempranamente se encontraron en profundidades anteriores.
         */
        searchResult = iterativeDeeping.searchBestMove(game, 3);
        bestMove = searchResult.getBestMove();
        Assert.assertEquals(Square.e2, bestMove.getFrom().getSquare());
        Assert.assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth > 1, tanto e2e4 como cualquier otro movimiento es valuado en 0
        Assert.assertEquals(0, searchResult.getEvaluation());
    }
}