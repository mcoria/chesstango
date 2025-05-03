package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.position.Position;
import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENParser;
import net.chesstango.board.representations.fen.FENBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class MirrorBuilderTest {

    @Test
    public void mirrorChessPosition() {
        MirrorPositionBuilder<Position> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new ChessPositionBuilder());
        mirrorChessPositionBuilder.withWhiteTurn(true);
        mirrorChessPositionBuilder.withPiece(Square.a1.getFile(), Square.a1.getRank(), Piece.ROOK_WHITE);
        mirrorChessPositionBuilder.withPiece(Square.e1.getFile(), Square.e1.getRank(), Piece.KING_WHITE);
        mirrorChessPositionBuilder.withPiece(Square.e8.getFile(), Square.e8.getRank(), Piece.KING_BLACK);
        Position mirrorPosition = mirrorChessPositionBuilder.getPositionRepresentation();

        assertEquals(Color.BLACK, mirrorPosition.getCurrentTurn());
        assertEquals(Piece.ROOK_BLACK, mirrorPosition.getPiece(Square.a8));
        assertEquals(Piece.KING_BLACK, mirrorPosition.getPiece(Square.e8));
        assertEquals(Piece.KING_WHITE, mirrorPosition.getPiece(Square.e1));
    }

    @Test
    public void mirrorChessPosition01() {
        Position position = Position.from(FEN.of(FENParser.INITIAL_FEN));

        MirrorPositionBuilder<Position> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new ChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        Position mirrorPosition = mirrorChessPositionBuilder.getPositionRepresentation();

        FENBuilder encoder = new FENBuilder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getPositionRepresentation().toString();

        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR b KQkq - 0 1", mirrorPositionEncoded);
    }


    @Test
    public void mirrorChessPosition02() {
        Position position = Position.from(FEN.of("4rr1k/pppb2bp/2q1n1p1/4p3/8/1BPPBN2/PP2QPP1/2KR3R w - - 8 20"));

        MirrorPositionBuilder<Position> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new ChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        Position mirrorPosition = mirrorChessPositionBuilder.getPositionRepresentation();

        FENBuilder encoder = new FENBuilder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getPositionRepresentation().toString();

        assertEquals("2kr3r/pp2qpp1/1bppbn2/8/4P3/2Q1N1P1/PPPB2BP/4RR1K b - - 8 20", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition03() {
        Position position = Position.from(FEN.of("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7"));

        MirrorPositionBuilder<Position> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new ChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        Position mirrorPosition = mirrorChessPositionBuilder.getPositionRepresentation();

        FENBuilder encoder = new FENBuilder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getPositionRepresentation().toString();

        assertEquals("r1bqkb1r/pp3ppp/2n5/2p1p3/1n6/2NPPN2/PP3PPP/R1BQKB1R w KQkq - 2 7", mirrorPositionEncoded);
    }

    @Test
    public void mirrorChessPosition04() {
        Position position = Position.from(FEN.of("rn1qkbnr/pp2ppp1/2p4p/3pPb2/3P2PP/8/PPP2P2/RNBQKBNR b KQkq g3 0 5"));

        MirrorPositionBuilder<Position> mirrorChessPositionBuilder = new MirrorPositionBuilder<>(new ChessPositionBuilder());

        position.constructChessPositionRepresentation(mirrorChessPositionBuilder);

        Position mirrorPosition = mirrorChessPositionBuilder.getPositionRepresentation();

        FENBuilder encoder = new FENBuilder();

        mirrorPosition.constructChessPositionRepresentation(encoder);

        String mirrorPositionEncoded = encoder.getPositionRepresentation().toString();

        assertEquals("rnbqkbnr/ppp2p2/8/3p2pp/3PpB2/2P4P/PP2PPP1/RN1QKBNR w KQkq g6 0 5", mirrorPositionEncoded);
    }
}
