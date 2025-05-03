package net.chesstango.board.representations.fen;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
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
        coder.withEnPassantSquare(Square.c3);

        String actual = coder.getEnPassant().toString();

        assertEquals("c3", actual);
    }

    @Test
    public void testEnPassantNull() {
        coder.withEnPassantSquare(null);

        String actual = coder.getEnPassant();

        assertEquals("-", actual);
    }

    @Test
    public void testCodePiecePlacementRank01() {
        StringBuilder stringBuilder = new StringBuilder();

        String actual = coder.codePiecePlacementRank(new Piece[]{Piece.BISHOP_WHITE, null, null, Piece.PAWN_WHITE, null, Piece.BISHOP_WHITE, null, Piece.PAWN_WHITE}, stringBuilder).toString();

        assertEquals("B2P1B1P", actual);
    }

    @Test
    public void testCodePiecePlacementRank02() {
        StringBuilder stringBuilder = new StringBuilder();

        String actual = coder.codePiecePlacementRank(new Piece[]{null, null, null, null, null, null, null, null}, stringBuilder);

        assertEquals("8", actual);
    }

    @Test
    public void testCodePiecePlacement03() {
        coder.withPiece(Square.a1, Piece.ROOK_WHITE);

        String actual = coder.getPiecePlacement();

        assertEquals("8/8/8/8/8/8/8/R7", actual);
    }

    @Test
    public void testCodePiecePlacement04() {
        coder.withPiece(Square.h1, Piece.ROOK_WHITE);

        String actual = coder.getPiecePlacement();

        assertEquals("8/8/8/8/8/8/8/7R", actual);
    }

    @Test
    public void testCodePiecePlacement05() {
        coder.withPiece(Square.a8, Piece.ROOK_BLACK);

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
        coder.withPiece(Square.a8, Piece.ROOK_BLACK);
        coder.withPiece(Square.b8, Piece.KNIGHT_BLACK);
        coder.withPiece(Square.c8, Piece.BISHOP_BLACK);
        coder.withPiece(Square.d8, Piece.QUEEN_BLACK);
        coder.withPiece(Square.e8, Piece.KING_BLACK);
        coder.withPiece(Square.f8, Piece.BISHOP_BLACK);
        coder.withPiece(Square.g8, Piece.KNIGHT_BLACK);
        coder.withPiece(Square.h8, Piece.ROOK_BLACK);

        coder.withPiece(Square.a7, Piece.PAWN_BLACK);
        coder.withPiece(Square.b7, Piece.PAWN_BLACK);
        coder.withPiece(Square.c7, Piece.PAWN_BLACK);
        coder.withPiece(Square.d7, Piece.PAWN_BLACK);
        coder.withPiece(Square.e7, Piece.PAWN_BLACK);
        coder.withPiece(Square.f7, Piece.PAWN_BLACK);
        coder.withPiece(Square.g7, Piece.PAWN_BLACK);
        coder.withPiece(Square.h7, Piece.PAWN_BLACK);


        coder.withPiece(Square.a2, Piece.PAWN_WHITE);
        coder.withPiece(Square.b2, Piece.PAWN_WHITE);
        coder.withPiece(Square.c2, Piece.PAWN_WHITE);
        coder.withPiece(Square.d2, Piece.PAWN_WHITE);
        coder.withPiece(Square.e2, Piece.PAWN_WHITE);
        coder.withPiece(Square.f2, Piece.PAWN_WHITE);
        coder.withPiece(Square.g2, Piece.PAWN_WHITE);
        coder.withPiece(Square.h2, Piece.PAWN_WHITE);

        coder.withPiece(Square.a1, Piece.ROOK_WHITE);
        coder.withPiece(Square.b1, Piece.KNIGHT_WHITE);
        coder.withPiece(Square.c1, Piece.BISHOP_WHITE);
        coder.withPiece(Square.d1, Piece.QUEEN_WHITE);
        coder.withPiece(Square.e1, Piece.KING_WHITE);
        coder.withPiece(Square.f1, Piece.BISHOP_WHITE);
        coder.withPiece(Square.g1, Piece.KNIGHT_WHITE);
        coder.withPiece(Square.h1, Piece.ROOK_WHITE);


        String actual = coder.getPiecePlacement();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);
    }

    @Test
    public void testGetFEN() {
        coder.withPiece(Square.a8, Piece.ROOK_BLACK);
        coder.withPiece(Square.b8, Piece.KNIGHT_BLACK);
        coder.withPiece(Square.c8, Piece.BISHOP_BLACK);
        coder.withPiece(Square.d8, Piece.QUEEN_BLACK);
        coder.withPiece(Square.e8, Piece.KING_BLACK);
        coder.withPiece(Square.f8, Piece.BISHOP_BLACK);
        coder.withPiece(Square.g8, Piece.KNIGHT_BLACK);
        coder.withPiece(Square.h8, Piece.ROOK_BLACK);

        coder.withPiece(Square.a7, Piece.PAWN_BLACK);
        coder.withPiece(Square.b7, Piece.PAWN_BLACK);
        coder.withPiece(Square.c7, Piece.PAWN_BLACK);
        coder.withPiece(Square.d7, Piece.PAWN_BLACK);
        coder.withPiece(Square.e7, Piece.PAWN_BLACK);
        coder.withPiece(Square.f7, Piece.PAWN_BLACK);
        coder.withPiece(Square.g7, Piece.PAWN_BLACK);
        coder.withPiece(Square.h7, Piece.PAWN_BLACK);


        coder.withPiece(Square.a2, Piece.PAWN_WHITE);
        coder.withPiece(Square.b2, Piece.PAWN_WHITE);
        coder.withPiece(Square.c2, Piece.PAWN_WHITE);
        coder.withPiece(Square.d2, Piece.PAWN_WHITE);
        coder.withPiece(Square.e2, Piece.PAWN_WHITE);
        coder.withPiece(Square.f2, Piece.PAWN_WHITE);
        coder.withPiece(Square.g2, Piece.PAWN_WHITE);
        coder.withPiece(Square.h2, Piece.PAWN_WHITE);

        coder.withPiece(Square.a1, Piece.ROOK_WHITE);
        coder.withPiece(Square.b1, Piece.KNIGHT_WHITE);
        coder.withPiece(Square.c1, Piece.BISHOP_WHITE);
        coder.withPiece(Square.d1, Piece.QUEEN_WHITE);
        coder.withPiece(Square.e1, Piece.KING_WHITE);
        coder.withPiece(Square.f1, Piece.BISHOP_WHITE);
        coder.withPiece(Square.g1, Piece.KNIGHT_WHITE);
        coder.withPiece(Square.h1, Piece.ROOK_WHITE);

        coder.withWhiteTurn(true);

        coder.withEnPassantSquare(null);

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

        String fen = coder.getPositionRepresentation().toString();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen.toString());
    }

    @Test
    public void test_encode_with_clocks2() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        game.executeMove(Square.g1, Square.f3);

        game.getPosition().constructChessPositionRepresentation(coder);

        String fen = coder.getPositionRepresentation().toString();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", fen.toString());
    }


    @Test
    public void test_encode_with_clocks3() {
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);

        game.executeMove(Square.g1, Square.f3)
                .executeMove(Square.g8, Square.f6);

        game.getPosition().constructChessPositionRepresentation(coder);

        String fen = coder.getPositionRepresentation().toString();

        assertEquals("rnbqkb1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 2 2", fen.toString());
    }

}
