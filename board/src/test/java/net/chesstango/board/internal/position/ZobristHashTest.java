package net.chesstango.board.internal.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.Position;
import net.chesstango.board.position.ZobristHash;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.polyglot.PolyglotKeyBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class ZobristHashTest {

    @Test
    public void testXorPosition01() {
        Position position = Position.from(FEN.of(FENParser.INITIAL_FEN));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosition(Square.f2));

        assertEquals(getPolyglotKey("rnbqkbnr/pppppppp/8/8/8/8/PPPPP1PP/RNBQKBNR w KQkq - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testXorPosition02() {
        Position position = Position.from(FEN.of(FENParser.INITIAL_FEN));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosition(Square.f2));
        zobristHash.xorPosition(PiecePositioned.of(Square.f3, Piece.PAWN_WHITE));

        assertEquals(getPolyglotKey("rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testMoveToEmptySquare() {
        Position position = Position.from(FEN.of(FENParser.INITIAL_FEN));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosition(Square.f2));
        zobristHash.xorPosition(PiecePositioned.of(Square.f3, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testTwoMoves() {
        Position position = Position.from(FEN.of(FENParser.INITIAL_FEN));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White Move
        zobristHash.xorPosition(position.getPosition(Square.f2));
        zobristHash.xorPosition(PiecePositioned.of(Square.f3, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        // Black Move
        zobristHash.xorPosition(position.getPosition(Square.d7));
        zobristHash.xorPosition(PiecePositioned.of(Square.d6, Piece.PAWN_BLACK));
        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("rnbqkbnr/ppp1pppp/3p4/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2"), zobristHash.getZobristHash());
    }

    @Test
    public void testCaptureMove() {
        Position position = Position.from(FEN.of("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2"));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White Move capture
        zobristHash.xorPosition(position.getPosition(Square.e4));
        zobristHash.xorPosition(position.getPosition(Square.d5));
        zobristHash.xorPosition(PiecePositioned.of(Square.d5, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2"), zobristHash.getZobristHash());
    }


    @Test
    public void testCastleWhiteKing() {
        Position position = Position.from(FEN.of("8/8/8/8/8/8/8/4K2R w K - 0 1"));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White move King
        zobristHash.xorPosition(position.getPosition(Square.e1));
        zobristHash.xorPosition(PiecePositioned.of(Square.g1, Piece.KING_WHITE));

        // White move Rook
        zobristHash.xorPosition(position.getPosition(Square.h1));
        zobristHash.xorPosition(PiecePositioned.of(Square.f1, Piece.ROOK_WHITE));

        zobristHash.xorCastleWhiteKing();

        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("8/8/8/8/8/8/8/5RK1 b - - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testCastleWhiteQueen() {
        Position position = Position.from(FEN.of("8/8/8/8/8/8/8/R3K3 w Q - 0 1"));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White move King
        zobristHash.xorPosition(position.getPosition(Square.e1));
        zobristHash.xorPosition(PiecePositioned.of(Square.c1, Piece.KING_WHITE));

        // White move Rook
        zobristHash.xorPosition(position.getPosition(Square.a1));
        zobristHash.xorPosition(PiecePositioned.of(Square.d1, Piece.ROOK_WHITE));

        zobristHash.xorCastleWhiteQueen();

        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("8/8/8/8/8/8/8/2KR4 b - - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testCastleBlackKing() {
        Position position = Position.from(FEN.of("4k2r/8/8/8/8/8/8/8 b k - 0 1"));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White move King
        zobristHash.xorPosition(position.getPosition(Square.e8));
        zobristHash.xorPosition(PiecePositioned.of(Square.g8, Piece.KING_BLACK));

        // White move Rook
        zobristHash.xorPosition(position.getPosition(Square.h8));
        zobristHash.xorPosition(PiecePositioned.of(Square.f8, Piece.ROOK_BLACK));

        zobristHash.xorCastleBlackKing();

        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("5rk1/8/8/8/8/8/8/8 w - - 0 1"), zobristHash.getZobristHash());
    }

    @Test
    public void testCastleBlackQueen() {
        Position position = Position.from(FEN.of("r3k3/8/8/8/8/8/8/8 b q - 0 1"));

        ZobristHash zobristHash = new ZobristHashImp();
        zobristHash.init(position);

        // White move King
        zobristHash.xorPosition(position.getPosition(Square.e8));
        zobristHash.xorPosition(PiecePositioned.of(Square.c8, Piece.KING_BLACK));

        // White move Rook
        zobristHash.xorPosition(position.getPosition(Square.a8));
        zobristHash.xorPosition(PiecePositioned.of(Square.d8, Piece.ROOK_BLACK));

        zobristHash.xorCastleBlackQueen();

        zobristHash.xorTurn();

        assertEquals(getPolyglotKey("2kr4/8/8/8/8/8/8/8 w - - 0 1"), zobristHash.getZobristHash());
    }


    private long getPolyglotKey(String fen) {
        PolyglotKeyBuilder polyglotKeyBuilder = new PolyglotKeyBuilder();
        FEN.of(fen).export(polyglotKeyBuilder);
        return polyglotKeyBuilder.getPositionRepresentation();
    }

}
