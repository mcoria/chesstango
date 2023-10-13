package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.search.builders.AlphaBetaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class RepetitionTest {
    private static final boolean PRINT_REPORT = true;
    private SearchMove moveFinder;
    private SearchMoveResult searchResult;

    @BeforeEach
    public void setup() {
        searchResult = null;

        moveFinder = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())

                .withTranspositionTable()

                .withStatistics()

                .build();
    }


    @Test
    public void testSearch_01() {
        Game game = FENDecoder.loadGame("8/7k/8/7Q/8/8/8/K7 b - - 0 1"); // Posicion inicial

        game.executeMove(Square.h7, Square.g8);
        game.executeMove(Square.h5, Square.e8);
        game.executeMove(Square.g8, Square.h7);
        game.executeMove(Square.e8, Square.h5);


        game.executeMove(Square.h7, Square.g8);
        game.executeMove(Square.h5, Square.e8);
        game.executeMove(Square.g8, Square.h7);

        //game.executeMove(Square.e8, Square.h5);
        //assertTrue(game.getStatus().isInProgress());

        /**
         * Va ganando, si repite el movimiento de reinda de h7 a g8 es draw por repeticion
         */
        moveFinder.setParameter(SearchParameter.MAX_DEPTH, 1);
        searchResult = moveFinder.search(game);

        Move bestMove = searchResult.getBestMove();

        assertFalse(Square.h7.equals(bestMove.getFrom().getSquare())
                && Square.g8.equals(bestMove.getTo().getSquare()));
    }


}
