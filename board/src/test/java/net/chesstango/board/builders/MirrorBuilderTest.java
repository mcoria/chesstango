package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.board.representations.fen.FENEncoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MirrorBuilderTest {

    @Test
    public void mirrorChessPosition() {
        MirrorChessPositionBuilder<ChessPosition> mirrorChessPositionBuilder = new MirrorChessPositionBuilder(new DefaultChessPositionBuilder());
        mirrorChessPositionBuilder.withTurn(Color.WHITE);
        mirrorChessPositionBuilder.withPiece(Square.a1, Piece.ROOK_WHITE);
        mirrorChessPositionBuilder.withPiece(Square.e1, Piece.KING_WHITE);
        mirrorChessPositionBuilder.withPiece(Square.e8, Piece.KING_BLACK);
        ChessPosition mirrorChessPosition = mirrorChessPositionBuilder.getChessRepresentation();

        assertEquals(Color.BLACK, mirrorChessPosition.getCurrentTurn());
        assertEquals(Piece.ROOK_BLACK, mirrorChessPosition.getPiece(Square.a8));
        assertEquals(Piece.KING_BLACK, mirrorChessPosition.getPiece(Square.e8));
        assertEquals(Piece.KING_WHITE, mirrorChessPosition.getPiece(Square.e1));
    }

    @Test
    public void mirrorChessPosition01() {
        ChessPosition position = new FEN(FENDecoder.INITIAL_FEN).toChessPosition();

        MirrorChessPositionBuilder<ChessPosition> mirrorChessPositionBuilder = new MirrorChessPositionBuilder(new DefaultChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        ChessPosition mirrorPosition = mirrorChessPositionBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation().toString();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", mirrorPositionEncoded);
    }


    @Test
    public void mirrorChessPosition02() {
        ChessPosition position = new FEN("4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20").toChessPosition();

        MirrorChessPositionBuilder<ChessPosition> mirrorChessPositionBuilder = new MirrorChessPositionBuilder(new DefaultChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        ChessPosition mirrorPosition = mirrorChessPositionBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation().toString();

        assertEquals("2kr3r/pp2qpp1/1bppbn2/8/4P3/2Q1N1P1/PPPB2BP/4RR1K b - - 8 20", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition03() {
        ChessPosition position = new FEN("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7").toChessPosition();

        MirrorChessPositionBuilder<ChessPosition> mirrorChessPositionBuilder = new MirrorChessPositionBuilder(new DefaultChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        ChessPosition mirrorPosition = mirrorChessPositionBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation().toString();

        assertEquals("r1bqkb1r/pp3ppp/2n5/2p1p3/1n6/2NPPN2/PP3PPP/R1BQKB1R w KQkq - 2 7", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition04() {
        ChessPosition position = new FEN("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5").toChessPosition();

        MirrorChessPositionBuilder<ChessPosition> mirrorChessPositionBuilder = new MirrorChessPositionBuilder(new DefaultChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        ChessPosition mirrorPosition = mirrorChessPositionBuilder.getChessRepresentation();

        FENEncoder encoder = new FENEncoder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getChessRepresentation().toString();

        assertEquals("rnbqkbnr/ppp2p2/8/3p2pp/3PpB2/2P4P/PP2PPP1/RN1QKBNR w KQkq g6 0 5", mirrorPositionEncoded);
    }
}
