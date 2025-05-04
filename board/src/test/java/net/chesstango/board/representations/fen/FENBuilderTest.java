package net.chesstango.board.representations.fen;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class FENBuilderTest {

    private FENBuilder coder;

    @BeforeEach
    public void setUp() throws Exception {
        coder = new FENBuilder();
    }

    @Test
    public void testTurnoWhite() {
        coder.withWhiteTurn(true);

        String actual = coder.getTurno();

        assertEquals("w", actual);
    }

    @Test
    public void testTurnoBlack() {
        coder.withWhiteTurn(false);

        String actual = coder.getTurno();

        assertEquals("b", actual);
    }

    @Test
    public void testEnPassantC3() {
        coder.withEnPassantSquare(2, 2);

        String actual = coder.getEnPassant();

        assertEquals("c3", actual);
    }

    @Test
    public void testEnPassantNull() {
        String actual = coder.getEnPassant();

        assertEquals("-", actual);
    }

    @Test
    public void testCodePiecePlacement03() {
        coder.withWhiteRook(0, 0);

        String actual = coder.getPiecePlacement();

        assertEquals("8/8/8/8/8/8/8/R7", actual);
    }

    @Test
    public void testCodePiecePlacement04() {
        coder.withWhiteRook(7, 0);

        String actual = coder.getPiecePlacement();

        assertEquals("8/8/8/8/8/8/8/7R", actual);
    }

    @Test
    public void testCodePiecePlacement05() {
        coder.withBlackRook(0, 7);

        String actual = coder.getPiecePlacement();

        assertEquals("r7/8/8/8/8/8/8/8", actual);
    }


    @Test
    public void withCastlingWhiteKingAllowed() {
        coder.withCastlingWhiteKingAllowed(true);

        String actual = coder.getEnroques();

        assertEquals("K", actual);
    }

    @Test
    public void withCastlingWhiteQueenAllowed() {
        coder.withCastlingWhiteQueenAllowed(true);

        String actual = coder.getEnroques();

        assertEquals("Q", actual);
    }

    @Test
    public void withCastlingBlackKingAllowed() {
        coder.withCastlingBlackKingAllowed(true);

        String actual = coder.getEnroques();

        assertEquals("k", actual);
    }

    @Test
    public void withCastlingBlackQueenAllowed() {
        coder.withCastlingBlackQueenAllowed(true);

        String actual = coder.getEnroques();

        assertEquals("q", actual);
    }

    @Test
    public void withoutEnroques() {
        String actual = coder.getEnroques();

        assertEquals("-", actual);
    }


    @Test
    public void testCodePiecePlacement06() {
        coder.withBlackRook(0, 7);
        coder.withBlackKnight(1, 7);
        coder.withBlackBishop(2, 7);
        coder.withBlackQueen(3, 7);
        coder.withBlackKing(4, 7);
        coder.withBlackBishop(5, 7);
        coder.withBlackKnight(6, 7);
        coder.withBlackRook(7, 7);

        coder.withBlackPawn(0, 6);
        coder.withBlackPawn(1, 6);
        coder.withBlackPawn(2, 6);
        coder.withBlackPawn(3, 6);
        coder.withBlackPawn(4, 6);
        coder.withBlackPawn(5, 6);
        coder.withBlackPawn(6, 6);
        coder.withBlackPawn(7, 6);


        coder.withWhitePawn(0, 1);
        coder.withWhitePawn(1, 1);
        coder.withWhitePawn(2, 1);
        coder.withWhitePawn(3, 1);
        coder.withWhitePawn(4, 1);
        coder.withWhitePawn(5, 1);
        coder.withWhitePawn(6, 1);
        coder.withWhitePawn(7, 1);

        coder.withWhiteRook(0, 0);
        coder.withWhiteKnight(1, 0);
        coder.withWhiteBishop(2, 0);
        coder.withWhiteQueen(3, 0);
        coder.withWhiteKing(4, 0);
        coder.withWhiteBishop(5, 0);
        coder.withWhiteKnight(6, 0);
        coder.withWhiteRook(7, 0);


        String actual = coder.getPiecePlacement();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);
    }

    @Test
    public void testGetFEN() {
        coder.withBlackRook(0, 7);
        coder.withBlackKnight(1, 7);
        coder.withBlackBishop(2, 7);
        coder.withBlackQueen(3, 7);
        coder.withBlackKing(4, 7);
        coder.withBlackBishop(5, 7);
        coder.withBlackKnight(6, 7);
        coder.withBlackRook(7, 7);

        coder.withBlackPawn(0, 6);
        coder.withBlackPawn(1, 6);
        coder.withBlackPawn(2, 6);
        coder.withBlackPawn(3, 6);
        coder.withBlackPawn(4, 6);
        coder.withBlackPawn(5, 6);
        coder.withBlackPawn(6, 6);
        coder.withBlackPawn(7, 6);

        coder.withWhitePawn(0, 1);
        coder.withWhitePawn(1, 1);
        coder.withWhitePawn(2, 1);
        coder.withWhitePawn(3, 1);
        coder.withWhitePawn(4, 1);
        coder.withWhitePawn(5, 1);
        coder.withWhitePawn(6, 1);
        coder.withWhitePawn(7, 1);

        coder.withWhiteRook(0, 0);
        coder.withWhiteKnight(1, 0);
        coder.withWhiteBishop(2, 0);
        coder.withWhiteQueen(3, 0);
        coder.withWhiteKing(4, 0);
        coder.withWhiteBishop(5, 0);
        coder.withWhiteKnight(6, 0);
        coder.withWhiteRook(7, 0);

        coder.withWhiteTurn(true);

        coder.withCastlingWhiteQueenAllowed(true);
        coder.withCastlingWhiteKingAllowed(true);
        coder.withCastlingBlackQueenAllowed(true);
        coder.withCastlingBlackKingAllowed(true);

        coder.withHalfMoveClock(3);
        coder.withFullMoveClock(5);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 3 5", fen.toString());
    }


    @Test
    public void test_encode_with_clocks1() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen.toString());
    }

    @Test
    public void test_encode_with_clocks2() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        game.executeMove(Square.g1, Square.f3);

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", fen.toString());
    }


    @Test
    public void test_encode_with_clocks3() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        game.executeMove(Square.g1, Square.f3)
                .executeMove(Square.g8, Square.f6);

        game.getPosition().constructChessPositionRepresentation(coder);

        FEN fen = coder.getPositionRepresentation();

        assertEquals("rnbqkb1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 2 2", fen.toString());
    }

    @Test
    public void testEnPassantSquareToString() {
        // Valid en passant square (bit 18 -> c3)
        long enPassantBitboard1 = 1L << 18;
        assertEquals("c3", FENBuilder.enPassantSquareToString(enPassantBitboard1));

        // Valid en passant square (bit 0 -> a1)
        long enPassantBitboard2 = 1L;
        assertEquals("a1", FENBuilder.enPassantSquareToString(enPassantBitboard2));

        // Valid en passant square (bit 63 -> h8)
        long enPassantBitboard3 = 1L << 63;
        assertEquals("h8", FENBuilder.enPassantSquareToString(enPassantBitboard3));

        // Valid en passant square (bit 18 -> c3)
        long enPassantBitboard4 = 1L << (5 * 8 + 1);
        assertEquals("b6", FENBuilder.enPassantSquareToString(enPassantBitboard4));

        // Invalid en passant square (no bits set)
        long enPassantBitboardInvalid = 0L;
        assertEquals("-", FENBuilder.enPassantSquareToString(enPassantBitboardInvalid));
    }
}
