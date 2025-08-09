
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public abstract class MateIn3Test {

    protected Search search;

    @Test //Roberto Grau vs. Edgar Colle
    public void test1() {
        Game game = Game.from(FEN.of("1k5r/pP3ppp/3p2b1/1BN1n3/1Q2P3/P1B5/KP3P1P/7q w - - 1 0"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KNIGHT_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.c5, smartMove.getFrom().getSquare());
        assertEquals(Square.a6, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Johannes Zukertort vs. William Norwood Potter
    public void test2() {
        Game game = Game.from(FEN.of("3r4/pR2N3/2pkb3/5p2/8/2B5/qP3PPP/4R1K1 w - - 1 0"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.c3, smartMove.getFrom().getSquare());
        assertEquals(Square.e5, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Ossip Bernstein vs. Alexander Kotov
    public void test3() {
        Game game = Game.from(FEN.of("R6R/1r3pp1/4p1kp/3pP3/1r2qPP1/7P/1P1Q3K/8 w - - 1 0"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.PAWN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.f4, smartMove.getFrom().getSquare());
        assertEquals(Square.f5, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Paul Vaitonis vs. Reuben Fine
    public void test4() {
        Game game = Game.from(FEN.of("4r1k1/5bpp/2p5/3pr3/8/1B3pPq/PPR2P2/2R2QK1 b - - 0 1"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.e5, smartMove.getFrom().getSquare());
        assertEquals(Square.e1, smartMove.getTo().getSquare());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

    @Test //Géza Maróczy vs. Heinrich Wolf
    public void test5() {
        Game game = Game.from(FEN.of("2r5/2p2k1p/pqp1RB2/2r5/PbQ2N2/1P3PP1/2P3P1/4R2K w - - 1 0"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.e6, smartMove.getFrom().getSquare());
        assertEquals(Square.e7, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //William Evans vs. Alexander MacDonnell
    public void test6() {
        Game game = Game.from(FEN.of("r3k2r/ppp2Npp/1b5n/4p2b/2B1P2q/BQP2P2/P5PP/RN5K w kq - 1 0"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.BISHOP_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.c4, smartMove.getFrom().getSquare());
        assertEquals(Square.b5, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test //Max Walter vs. Emanuel Lasker
    public void test7() {
        Game game = Game.from(FEN.of("4r1k1/3n1ppp/4r3/3n3q/Q2P4/5P2/PP2BP1P/R1B1R1K1 b - - 0 1"));

        SearchResult searchResult = search.startSearch(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.e6, smartMove.getFrom().getSquare());
        assertEquals(Square.g6, smartMove.getTo().getSquare());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }
}
