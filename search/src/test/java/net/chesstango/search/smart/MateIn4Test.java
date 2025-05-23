
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 */
public abstract class MateIn4Test {

    protected Search search;

    @Test //Viktor Korchnoi vs. Peterson
    public void test1() {
        Game game = Game.fromFEN("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.g4, smartMove.getFrom().getSquare());
        assertEquals(Square.g7, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }


    @Test //Paul Keres vs. Arturo Pomar Salamanca
    public void test2() {
        Game game = Game.fromFEN("7R/r1p1q1pp/3k4/1p1n1Q2/3N4/8/1PP2PPP/2B3K1 w - - 1 0");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.h8, smartMove.getFrom().getSquare());
        assertEquals(Square.d8, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Alexander Meek vs. Paul Morphy
    public void test3() {
        Game game = Game.fromFEN("Q7/p1p1q1pk/3p2rp/4n3/3bP3/7b/PP3PPK/R1B2R2 b - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.h3, smartMove.getFrom().getSquare());
        assertEquals(Square.g2, smartMove.getTo().getSquare());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

    @Test
    public void test8() {
        Game game = Game.fromFEN("1k1r3r/pp6/2P1bp2/2R1p3/Q3Pnp1/P2q4/1BR3B1/6K1 b - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KNIGHT_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.f4, smartMove.getFrom().getSquare());
        assertEquals(Square.h3, smartMove.getTo().getSquare());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

}
