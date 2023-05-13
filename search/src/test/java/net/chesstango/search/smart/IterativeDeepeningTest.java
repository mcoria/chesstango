package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorByFEN;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.alphabeta.MinMaxPruning;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepeningTest {

    private IterativeDeepening iterativeDeepening;

    private SearchSmart smart;

    @BeforeEach
    public void setup() {
        smart = new MinMaxPruning();

        iterativeDeepening = new IterativeDeepening(smart);
    }

    @Test
    @Disabled //Se deberia resolver cuando ordenamos considerando TT
    public void testPasarPorMaximo() {
        GameEvaluatorByFEN evaluatorMock = new GameEvaluatorByFEN();
        evaluatorMock.setDefaultValue(0);
        evaluatorMock.addEvaluation("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", 1);

        //smart.setGameEvaluator(evaluatorMock);

        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        SearchMoveResult searchResult = iterativeDeepening.search(game, 1);
        Move bestMove = searchResult.getBestMove();
        assertEquals(Square.e2, bestMove.getFrom().getSquare());
        assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth 1 el movimiento e2e4 es evaluado en 1
        assertEquals(1, searchResult.getEvaluation());

        /**
         * Repetimos la busqueda en depth = 3, acá la evaluacion de todos los movimientos es la misma.
         * Lo que queremos es priorizar aquellos movimientos que tempranamente se encontraron en profundidades anteriores.
         */
        searchResult = iterativeDeepening.search(game, 3);
        bestMove = searchResult.getBestMove();
        assertEquals(Square.e2, bestMove.getFrom().getSquare());
        assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth > 1, tanto e2e4 como cualquier otro movimiento es valuado en 0
        assertEquals(0, searchResult.getEvaluation());
    }
}
