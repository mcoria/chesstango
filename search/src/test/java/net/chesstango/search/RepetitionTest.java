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

                .withStatistics()

                .build();
    }


    @Test
    public void testSearch_01() {
        Game game = FENDecoder.loadGame("6k1/8/8/7Q/8/8/8/K7 w - - 0 1"); // Posicion inicial
        game.detectRepetitions(true);

        game.executeMove(Square.h5, Square.e8);
        game.executeMove(Square.g8, Square.h7);
        game.executeMove(Square.e8, Square.h5);
        game.executeMove(Square.h7, Square.g8); //2da vez donde se repite la posicion inicial

        game.executeMove(Square.h5, Square.e8);
        game.executeMove(Square.g8, Square.h7);
        game.executeMove(Square.e8, Square.h5);
        //game.executeMove(Square.h7, Square.g8); //3ra vez donde se repite la posicion inicial


        /**
         * Va ganando, si repite el movimiento de reinda de h7 a g8 es draw por repeticion
         */
        searchResult = moveFinder.search(game, 5);

        Move bestMove = searchResult.getBestMove();

        assertFalse(Square.h7.equals(bestMove.getFrom().getSquare())
                && Square.g8.equals(bestMove.getTo().getSquare()));
    }


}
