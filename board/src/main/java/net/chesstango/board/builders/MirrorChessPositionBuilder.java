package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 */
public class MirrorChessPositionBuilder<T> implements ChessPositionBuilder<T> {

    private final ChessPositionBuilder<T> chessPositionBuilder;

    public MirrorChessPositionBuilder(ChessPositionBuilder<T> chessPositionBuilder) {
        this.chessPositionBuilder = chessPositionBuilder;
    }

    @Override
    public ChessPositionBuilder<T> withTurn(Color turn) {
        chessPositionBuilder.withTurn(turn.oppositeColor());
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withEnPassantSquare(Square enPassantSquare) {
        chessPositionBuilder.withEnPassantSquare(enPassantSquare == null ? null : enPassantSquare.getMirrorSquare());
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        chessPositionBuilder.withCastlingBlackQueenAllowed(castlingWhiteQueenAllowed);
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        chessPositionBuilder.withCastlingBlackKingAllowed(castlingWhiteKingAllowed);
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        chessPositionBuilder.withCastlingWhiteQueenAllowed(castlingBlackQueenAllowed);
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        chessPositionBuilder.withCastlingWhiteKingAllowed(castlingBlackKingAllowed);
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withPiece(Square square, Piece piece) {
        chessPositionBuilder.withPiece(square.getMirrorSquare(), piece == null ? null : piece.getOpposite());
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withHalfMoveClock(int halfMoveClock) {
        chessPositionBuilder.withHalfMoveClock(halfMoveClock);
        return this;
    }

    @Override
    public ChessPositionBuilder<T> withFullMoveClock(int fullMoveClock) {
        chessPositionBuilder.withFullMoveClock(fullMoveClock);
        return this;
    }

    @Override
    public T getChessRepresentation() {
        return chessPositionBuilder.getChessRepresentation();
    }
}
