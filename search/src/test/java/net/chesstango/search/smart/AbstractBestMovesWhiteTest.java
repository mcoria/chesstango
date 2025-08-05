
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
public abstract class AbstractBestMovesWhiteTest {

    protected Search search;

    @Test
    public void test_moveQueen() {
        // hay que sacar a la reina blanca de donde esta, sino se la morfa el caballo
        Game game = Game.from(FEN.of("r1bqkb1r/ppp2ppp/5n2/3p4/1n1p3P/N2Q1N2/PPP1PPP1/R1B1KB1R w KQkq - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 2);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.d3, smartMove.getFrom().getSquare());
        assertNotEquals(Square.a6, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.c4, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.c3, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.e3, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.f5, smartMove.getTo().getSquare(), "Queen captured by bishop");
        assertNotEquals(Square.g6, smartMove.getTo().getSquare(), "Queen captured by pawn");
    }

    @Test
    public void test_imminentMateIn2Moves() {
        // White will be in checkmate in the next 1 move
        Game game = Game.from(FEN.of("6k1/3r4/1b3p2/8/1Pp5/8/2Kq2p1/8 w - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 2);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.c2, smartMove.getFrom().getSquare());
        assertEquals(Square.b1, smartMove.getTo().getSquare(), "There is no other option for black King");

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }


    @Test
    public void test_imminentMateIn4Moves() {
        // White will be in checkmate in the next 2 move
        Game game = Game.from(FEN.of("6k1/3r4/1b3p2/8/1Pp5/6p1/2Kq4/8 w - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 4);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.c2, smartMove.getFrom().getSquare());
        assertEquals(Square.b1, smartMove.getTo().getSquare(), "There is no other option for black King");

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

    @Test
    public void test_Mate() {
        // White can win the game in the next move
        Game game = Game.from(FEN.of("1Q6/8/8/k1K5/3P4/2P1PP2/6P1/5r2 w - - 1 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH, 5);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.b8, smartMove.getFrom().getSquare());

        Square to = smartMove.getTo().getSquare();
        assertTrue(Square.a8.equals(to) || Square.a7.equals(to) || Square.b6.equals(to) || Square.b5.equals(to) || Square.b4.equals(to));

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Max Walter vs. Emanuel Lasker
    public void test_MateInThree() {

        Game game = Game.from(FEN.of("r1b1r1k1/pp2bp1p/5p2/q2p4/3N3Q/4R3/3N1PPP/4R1K1 w - - 0 1"));

        search.setSearchParameter(SearchParameter.MAX_DEPTH , 5);
        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.e3, smartMove.getFrom().getSquare());
        assertEquals(Square.g3, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

}
