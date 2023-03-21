package net.chesstango.board.position.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.polyglot.PolyglotEncoder;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class ZobristHashTest {

    @Test
    public void testXorPosition01(){
        ChessPosition position = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        ZobristHash zobristHash = new ZobristHash();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosicion(Square.f2));

        Assert.assertEquals(PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/8/8/PPPPP1PP/RNBQKBNR w KQkq - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testXorPosition02(){
        ChessPosition position = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        ZobristHash zobristHash = new ZobristHash();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosicion(Square.f2));
        zobristHash.xorPosition(PiecePositioned.getPiecePositioned(Square.f3, Piece.PAWN_WHITE));

        Assert.assertEquals(PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testMoveToEmptySquare(){
        ChessPosition position = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        ZobristHash zobristHash = new ZobristHash();
        zobristHash.init(position);

        zobristHash.xorPosition(position.getPosicion(Square.f2));
        zobristHash.xorPosition(PiecePositioned.getPiecePositioned(Square.f3, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        Assert.assertEquals(PolyglotEncoder.getKey("rnbqkbnr/pppppppp/8/8/8/5P2/PPPPP1PP/RNBQKBNR b KQkq - 0 1").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testTwoMoves(){
        ChessPosition position = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        ZobristHash zobristHash = new ZobristHash();
        zobristHash.init(position);

        // White Move
        zobristHash.xorPosition(position.getPosicion(Square.f2));
        zobristHash.xorPosition(PiecePositioned.getPiecePositioned(Square.f3, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        // Black Move
        zobristHash.xorPosition(position.getPosicion(Square.d7));
        zobristHash.xorPosition(PiecePositioned.getPiecePositioned(Square.d6, Piece.PAWN_BLACK));
        zobristHash.xorTurn();

        Assert.assertEquals(PolyglotEncoder.getKey("rnbqkbnr/ppp1pppp/3p4/8/8/5P2/PPPPP1PP/RNBQKBNR w KQkq - 0 2").longValue(), zobristHash.getZobristHash());
    }

    @Test
    public void testCaptureMove(){
        ChessPosition position = FENDecoder.loadChessPosition("rnbqkbnr/ppp1pppp/8/3p4/4P3/8/PPPP1PPP/RNBQKBNR w KQkq d6 0 2 ");

        ZobristHash zobristHash = new ZobristHash();
        zobristHash.init(position);

        // White Move capture
        zobristHash.xorPosition(position.getPosicion(Square.e4));
        zobristHash.xorPosition(position.getPosicion(Square.d5));
        zobristHash.xorPosition(PiecePositioned.getPiecePositioned(Square.d5, Piece.PAWN_WHITE));
        zobristHash.xorTurn();

        Assert.assertEquals(PolyglotEncoder.getKey("rnbqkbnr/ppp1pppp/8/3P4/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 2").longValue(), zobristHash.getZobristHash());
    }

}
