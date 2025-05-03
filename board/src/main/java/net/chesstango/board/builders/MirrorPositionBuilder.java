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
    public PositionBuilder<T> withTurn(Color turn) {
        positionBuilder.withTurn(turn.oppositeColor());
        return this;
    }

    @Override
    public PositionBuilder<T> withEnPassantSquare(Square enPassantSquare) {
        positionBuilder.withEnPassantSquare(enPassantSquare == null ? null : enPassantSquare.getMirrorSquare());
        return this;
    }

    @Override
    public PositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        positionBuilder.withCastlingBlackQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        positionBuilder.withCastlingBlackKingAllowed(castlingWhiteKingAllowed);
        return this;
    }

    @Override
    public PositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        positionBuilder.withCastlingWhiteQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public PositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        positionBuilder.withCastlingWhiteKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public PositionBuilder<T> withPiece(Square square, Piece piece) {
        positionBuilder.withPiece(square.getMirrorSquare(), piece == null ? null : piece.getOpposite());
        return this;
    }

    @Override
    public PositionBuilder<T> withHalfMoveClock(int halfMoveClock) {
        positionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public PositionBuilder<T> withFullMoveClock(int fullMoveClock) {
        positionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

    @Override
    public T getPositionRepresentation() {
        return positionBuilder.getPositionRepresentation();
    }
}
