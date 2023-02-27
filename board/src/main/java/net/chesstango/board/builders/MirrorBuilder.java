package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

public class MirrorBuilder<T> implements ChessRepresentationBuilder<T> {

    private ChessRepresentationBuilder<T> chessPositionBuilder;

    public MirrorBuilder(ChessRepresentationBuilder<T> chessPositionBuilder) {
        this.chessPositionBuilder = chessPositionBuilder;
    }

    @Override
    public ChessRepresentationBuilder<T> withTurn(Color turn) {
        chessPositionBuilder.withTurn(turn.oppositeColor());
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withEnPassantSquare(Square enPassantSquare) {
        chessPositionBuilder.withEnPassantSquare(enPassantSquare == null ? null : enPassantSquare.getMirrorSquare());
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        chessPositionBuilder.withCastlingBlackQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        chessPositionBuilder.withCastlingBlackKingAllowed(castlingWhiteKingAllowed);
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        chessPositionBuilder.withCastlingWhiteQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        chessPositionBuilder.withCastlingWhiteKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withPiece(Square square, Piece piece) {
        chessPositionBuilder.withPiece(square.getMirrorSquare(), piece == null ? null : piece.getOpposite());
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withHalfMoveClock(int halfMoveClock) {
        chessPositionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public ChessRepresentationBuilder<T> withFullMoveClock(int fullMoveClock) {
        chessPositionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

    @Override
    public T getChessRepresentation() {
        return chessPositionBuilder.getChessRepresentation();
    }
}
