package net.chesstango.board.builders;

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
    public MirrorPositionBuilder<T> withWhiteKing(int file, int rank) {
        positionBuilder.withBlackKing(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withWhiteQueen(int file, int rank) {
        positionBuilder.withBlackQueen(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withWhiteRook(int file, int rank) {
        positionBuilder.withBlackRook(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withWhiteBishop(int file, int rank) {
        positionBuilder.withBlackBishop(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withWhiteKnight(int file, int rank) {
        positionBuilder.withBlackKnight(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withWhitePawn(int file, int rank) {
        positionBuilder.withBlackPawn(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackKing(int file, int rank) {
        positionBuilder.withWhiteKing(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackQueen(int file, int rank) {
        positionBuilder.withWhiteQueen(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackRook(int file, int rank) {
        positionBuilder.withWhiteRook(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackBishop(int file, int rank) {
        positionBuilder.withWhiteBishop(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackKnight(int file, int rank) {
        positionBuilder.withWhiteKnight(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withBlackPawn(int file, int rank) {
        positionBuilder.withWhitePawn(file, 7- rank);
        return this;
    }

    @Override
    public MirrorPositionBuilder<T> withEnPassantSquare(int file, int rank) {
        positionBuilder.withEnPassantSquare(file, 7 - rank);
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
