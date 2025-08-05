package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.evaluation.evaluators.EvaluatorByFEN;
import net.chesstango.search.SearchResult;
import net.chesstango.search.smart.alphabeta.AlphaBetaFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class IterativeDeepeningTest {

    private IterativeDeepening iterativeDeepening;

    private SearchAlgorithm smart;

    @BeforeEach
    public void setup() {
        smart = new AlphaBetaFacade();

        iterativeDeepening = new IterativeDeepening(smart, null);
    }

    @Test
    @Disabled //Se deberia resolver cuando ordenamos considerando TT
    public void testPasarPorMaximo() {
        EvaluatorByFEN evaluatorMock = new EvaluatorByFEN();
        evaluatorMock.setDefaultValue(0);
        evaluatorMock.addEvaluation("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", 1);

        //smart.setGameEvaluator(evaluatorMock);

        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        SearchResult searchResult = iterativeDeepening.startSearch(game);
        Move bestMove = searchResult.getBestMove();
        assertEquals(Square.e2, bestMove.getFrom().getSquare());
        assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth 1 el movimiento e2e4 es evaluado en 1
        assertEquals(1, searchResult.getBestEvaluation());

        /**
         * Repetimos la busqueda en depth = 3, acá la evaluacion de todos los movimientos es la misma.
         * Lo que queremos es priorizar aquellos movimientos que tempranamente se encontraron en profundidades anteriores.
         */
        searchResult = iterativeDeepening.startSearch(game);
        bestMove = searchResult.getBestMove();
        assertEquals(Square.e2, bestMove.getFrom().getSquare());
        assertEquals(Square.e4, bestMove.getTo().getSquare());

        // En depth > 1, tanto e2e4 como cualquier otro movimiento es valuado en 0
        assertEquals(0, searchResult.getBestEvaluation());
    }
}
