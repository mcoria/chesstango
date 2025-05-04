package net.chesstango.board.representations.move;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Mauricio Coria
 *
 */
public class SANDecoderTest {

    private SANDecoder decoder = new SANDecoder();

    @Test
    public void test_bishop() {
        Game game = Game.fromFEN("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.c1, Square.d2);
        Move decodedMove = decoder.decode("Bd2", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawn_push01() {
        Game game = Game.fromFEN("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.a2, Square.a3);
        Move decodedMove = decoder.decode("a3", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawnMove1() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        Move expectedMove = game.getMove(Square.e2, Square.e3);
        Move decodedMove = decoder.decode("e3", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void
    test_pawnMove_capture() {
        Game game = Game.fromFEN("rnbqkbnr/ppp1p1pp/8/3p1p2/2P1P3/8/PP1P1PPP/RNBQKBNR w KQkq f6 0 3");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.e4, Square.f5);
        decodedMove = decoder.decode("exf5", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);

        expectedMove = game.getMove(Square.c4, Square.d5);
        decodedMove = decoder.decode("cxd5", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);

        expectedMove = game.getMove(Square.e4, Square.d5);
        decodedMove = decoder.decode("exd5", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_castling() {
        Game game = Game.fromFEN("3b3k/2P5/8/8/4P3/8/PP1P1PPP/R3K2R w KQ - 0 1 ");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.e1, Square.c1);
        decodedMove = decoder.decode("O-O-O", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);

        expectedMove = game.getMove(Square.e1, Square.g1);
        decodedMove = decoder.decode("O-O", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_knight_move01() {
        Game game = Game.fromFEN("r1k4r/ppp4p/2nb1pq1/3p1np1/4p1Q1/4P3/PPPPNPPP/RNB1K2R w KQ - 0 1");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.b1, Square.c3);
        decodedMove = decoder.decode("Nbc3", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_knight_move02() {
        Game game = Game.fromFEN("rk2q3/ppp5/6n1/2b5/4pp2/P1N5/1PPPKPRP/R1B5 b - - 19 47");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.g6, Square.h4);
        decodedMove = decoder.decode("Nh4", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }


    @Test
    public void test_knight_move03() {
        Game game = Game.fromFEN("rk2q3/ppp5/5p2/2b2np1/4p3/P1N1Pn2/1PPPKPRP/R1B5 b - - 3 34");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.f3, Square.h4);
        decodedMove = decoder.decode("N3h4", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);

        expectedMove = game.getMove(Square.f5, Square.h4);
        decodedMove = decoder.decode("N5h4", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_knight_move04() {
        Game game = Game.fromFEN("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.g1, Square.e2);
        Move decodedMove = decoder.decode("Nge2", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_knight_move05() {
        Game game = Game.fromFEN("rnbqk2r/pp1p1ppp/4pn2/2p5/1bPP4/2N1P3/PP3PPP/R1BQKBNR w KQkq c6 0 5");

        Move expectedMove = game.getMove(Square.g1, Square.f3);
        Move decodedMove = decoder.decode("Nf3", game.getPossibleMoves());

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawnMove_promotion01() {
        Game game = Game.fromFEN("3b3k/2P5/8/8/4P3/8/PP1P1PPP/R3K2R w KQ - 0 1");

        Move expectedMove = null;
        Move decodedMove = null;

        expectedMove = game.getMove(Square.c7, Square.c8, Piece.ROOK_WHITE);
        decodedMove = decoder.decode("c8=R", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);

        expectedMove = game.getMove(Square.c7, Square.d8, Piece.QUEEN_WHITE);
        decodedMove = decoder.decode("cxd8=Q", game.getPossibleMoves());
        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawnMove_promotion02() {
        Game game = Game.fromFEN("8/PR1nk2p/4p1p1/8/3p3P/5K2/8/8 w - - 9 54");

        Move expectedMove = game.getMove(Square.a7, Square.a8, Piece.QUEEN_WHITE);
        assertNotNull(expectedMove);

        Move decodedMove = decoder.decode("a8Q", game.getPossibleMoves());
        assertNotNull(decodedMove);

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_pawnMove_promotion03() {
        Game game = Game.fromFEN("2k1r3/1p1b4/p1p5/P1P5/1P1P1p2/3B1K2/6pP/3R4 b - - 0 37");

        Move expectedMove = game.getMove(Square.g2, Square.g1, Piece.QUEEN_BLACK);
        assertNotNull(expectedMove);

        Move decodedMove = decoder.decode("g1", game.getPossibleMoves());
        assertNotNull(decodedMove);

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_check01() {
        Game game = Game.fromFEN("6k1/5p2/p6p/1pQ3P1/2n1B3/2P1PpP1/q4r2/4R1K1 b - - 0 37");

        Move expectedMove = game.getMove(Square.f2, Square.f1);
        assertNotNull(expectedMove);

        Move decodedMove =decoder.decode("Rf1+", game.getPossibleMoves());
        assertNotNull(decodedMove);

        assertEquals(expectedMove, decodedMove);
    }

    @Test
    public void test_check02() {
        Game game = Game.fromFEN("r1bq1rk1/ppp2ppp/3p1n2/3Np3/1bPnP3/5NP1/PP1P1PBP/R1BQ1RK1 b - - 3 8");

        Move expectedMove = game.getMove(Square.d4, Square.f3);
        assertNotNull(expectedMove);

        Move decodedMove = decoder.decode("Nxf3+", game.getPossibleMoves());
        assertNotNull(decodedMove);

        assertEquals(expectedMove, decodedMove);
    }
}
