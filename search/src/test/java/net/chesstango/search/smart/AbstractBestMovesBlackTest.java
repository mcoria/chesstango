
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import net.chesstango.search.SearchParameter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractBestMovesBlackTest {


    protected Search search;

    @Test
    public void test_moveQueen() {
        // hay que sacar a la reina negra de donde esta, sino se la morfa el caballo
        Game game = Game.from(FEN.of("r1b1kb1r/ppp1ppp1/n2q1n2/1N1P3p/3P4/5N2/PPP2PPP/R1BQKB1R b KQkq - 1 1"));

		search.setSearchParameter(SearchParameter.MAX_DEPTH, 2);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().piece());
        assertEquals(Square.d6, smartMove.getFrom().square());
        assertNotEquals(Square.a3, smartMove.getTo().square(), "Queen captured by pawn");
        assertNotEquals(Square.c5, smartMove.getTo().square(), "Queen captured by pawn");
        assertNotEquals(Square.c6, smartMove.getTo().square(), "Queen captured by pawn");
        assertNotEquals(Square.e6, smartMove.getTo().square(), "Queen captured by pawn");
        assertNotEquals(Square.f4, smartMove.getTo().square(), "Queen captured by bishop");
        assertNotEquals(Square.g3, smartMove.getTo().square(), "Queen captured by pawn");
    }

    @Test
    public void test_imminentMateIn2Moves() {
        // Black will be in checkmate in the next 1 move
        Game game = Game.from(FEN.of("8/2kQ2P1/8/1pP5/8/1B3P2/3R4/6K1 b - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 2);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_BLACK, smartMove.getFrom().piece());
        assertEquals(Square.c7, smartMove.getFrom().square());
        assertEquals(Square.b8, smartMove.getTo().square(), "There is no other option for black King");

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }


    @Test
    public void test_imminentMateIn4Moves() {
        // Black will be in checkmate in the next 2 move
        Game game = Game.from(FEN.of("8/2kQ4/6P1/1pP5/8/1B3P2/3R4/6K1 b - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 4);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_BLACK, smartMove.getFrom().piece());
        assertEquals(Square.c7, smartMove.getFrom().square());
        assertEquals(Square.b8, smartMove.getTo().square(), "There is no other option for black King");

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }


    @Test
    public void test_Mate() {
        // Black can win the game in the next move
        Game game = Game.from(FEN.of("5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 5);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().piece());
        assertEquals(Square.b1, smartMove.getFrom().square());

        Square to = smartMove.getTo().square();
        assertTrue(Square.a1.equals(to) || Square.a2.equals(to) || Square.b3.equals(to) || Square.b4.equals(to) || Square.b5.equals(to));

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

    @Test //Max Walter vs. Emanuel Lasker
    public void test_MateInThree() {

        Game game = Game.from(FEN.of("4r1k1/3n1ppp/4r3/3n3q/Q2P4/5P2/PP2BP1P/R1B1R1K1 b - - 0 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 5);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().piece());
        assertEquals(Square.e6, smartMove.getFrom().square());
        assertEquals(Square.g6, smartMove.getTo().square());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

}
