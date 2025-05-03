package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.representations.PositionBuilder;

/**
 * @author Mauricio Coria
 */
public class MirrorPositionBuilder<T> implements PositionBuilder<T> {

    private final PositionBuilder<T> positionBuilder;

    public MirrorPositionBuilder(PositionBuilder<T> positionBuilder) {
        this.positionBuilder = positionBuilder;
    }

    @Override
    public MirrorPositionBuilder<T> withWhiteTurn(boolean whiteTurn) {
        positionBuilder.withWhiteTurn(!whiteTurn);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withEnPassantSquare(Square enPassantSquare) {
        positionBuilder.withEnPassantSquare(enPassantSquare == null ? null : enPassantSquare.getMirrorSquare());
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        positionBuilder.withCastlingBlackQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        positionBuilder.withCastlingBlackKingAllowed(castlingWhiteKingAllowed);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        positionBuilder.withCastlingWhiteQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        positionBuilder.withCastlingWhiteKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withPiece(Square square, Piece piece) {
        positionBuilder.withPiece(square.getMirrorSquare(), piece == null ? null : piece.getOpposite());
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withHalfMoveClock(int halfMoveClock) {
        positionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withFullMoveClock(int fullMoveClock) {
        positionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

    @Override
    public T getPositionRepresentation() {
        return positionBuilder.getPositionRepresentation();
    }
}
