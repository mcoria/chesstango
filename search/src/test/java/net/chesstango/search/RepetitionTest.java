package net.chesstango.search;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.evaluators.EvaluatorByMaterial;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.builders.AlphaBetaBuilder;
import net.chesstango.search.visitors.SetMaxDepthVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Mauricio Coria
 */
public class RepetitionTest {
    private static final boolean PRINT_REPORT = false;
    private Search search;
    private SearchResult searchResult;

    @BeforeEach
    public void setup() {
        searchResult = null;

        search = new AlphaBetaBuilder()
                .withGameEvaluator(new EvaluatorByMaterial())

                .withTranspositionTable()

                .withStatistics()

                .build();
    }


    @Test
    public void testSearch_01() {
        Game game = Game.from(FEN.of("8/7k/8/7Q/8/8/8/K7 b - - 0 1")); // Posicion inicial

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
        search.accept(new SetMaxDepthVisitor(1));
        searchResult = search.startSearch(game);

        Move bestMove = searchResult.getBestMove();

        assertFalse(Square.h7.equals(bestMove.getFrom().square())
                && Square.g8.equals(bestMove.getTo().square()));
    }


}
