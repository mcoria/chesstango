package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import org.junit.Assert;
import org.junit.Test;

public class MirrorBuilderTest {

    @Test
    public void mirrorChessPosition(){
        MirrorBuilder<ChessPosition> mirrorBuilder = new MirrorBuilder(new ChessPositionBuilder());
        mirrorBuilder.withTurn(Color.WHITE);
        mirrorBuilder.withPiece(Square.a1, Piece.ROOK_WHITE);
        mirrorBuilder.withPiece(Square.e1, Piece.KING_WHITE);
        mirrorBuilder.withPiece(Square.e8, Piece.KING_BLACK);
        ChessPosition mirrorChessPosition = mirrorBuilder.getChessRepresentation();

        Assert.assertEquals(Color.BLACK, mirrorChessPosition.getCurrentTurn());
        Assert.assertEquals(Piece.ROOK_BLACK, mirrorChessPosition.getPiece(Square.a8));
        Assert.assertEquals(Piece.KING_BLACK, mirrorChessPosition.getPiece(Square.e8));
        Assert.assertEquals(Piece.KING_WHITE, mirrorChessPosition.getPiece(Square.e1));
    }

    @Test
    public void mirrorChessPosition01(){
        ChessPosition position = FENDecoder.loadChessPosition(FENDecoder.INITIAL_FEN);

        MirrorBuilder<ChessPosition> mirrorBuilder = new MirrorBuilder(new ChessPositionBuilder());

        position.constructBoardRepresentation(mirrorBuilder);

        ChessPosition mirrorPosition = mirrorBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructBoardRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation();

        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", mirrorPositionEncoded);
    }


    @Test
    public void mirrorChessPosition02(){
        ChessPosition position = FENDecoder.loadChessPosition("4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20");

        MirrorBuilder<ChessPosition> mirrorBuilder = new MirrorBuilder(new ChessPositionBuilder());

        position.constructBoardRepresentation(mirrorBuilder);

        ChessPosition mirrorPosition = mirrorBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructBoardRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation();

        Assert.assertEquals("2kr3r/pp2qpp1/1bppbn2/8/4P3/2Q1N1P1/PPPB2BP/4RR1K b - - 8 20", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition03(){
        ChessPosition position = FENDecoder.loadChessPosition("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7");

        MirrorBuilder<ChessPosition> mirrorBuilder = new MirrorBuilder(new ChessPositionBuilder());

        position.constructBoardRepresentation(mirrorBuilder);

        ChessPosition mirrorPosition = mirrorBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructBoardRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation();

        Assert.assertEquals("r1bqkb1r/pp3ppp/2n5/2p1p3/1n6/2NPPN2/PP3PPP/R1BQKB1R w KQkq - 2 7", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition04(){
        ChessPosition position = FENDecoder.loadChessPosition("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5");

        MirrorBuilder<ChessPosition> mirrorBuilder = new MirrorBuilder(new ChessPositionBuilder());

        position.constructBoardRepresentation(mirrorBuilder);

        ChessPosition mirrorPosition = mirrorBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructBoardRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation();

        Assert.assertEquals("rnbqkbnr/ppp2p2/8/3p2pp/3PpB2/2P4P/PP2PPP1/RN1QKBNR w KQkq g6 0 5", mirrorPositionEncoded);
    }
}
