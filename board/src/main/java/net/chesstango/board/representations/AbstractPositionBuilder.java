package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractPositionBuilder<T> implements PositionBuilder<T> {
    protected boolean whiteTurn;
    protected long enPassantSquare;
    protected boolean castlingBlackKingAllowed;
    protected boolean castlingBlackQueenAllowed;
    protected boolean castlingWhiteKingAllowed;
    protected boolean castlingWhiteQueenAllowed;
    protected int halfMoveClock;
    protected int fullMoveClock;

    protected final Piece[][] board = new Piece[8][8];

    @Override
    public AbstractPositionBuilder<T> withPiece(Square square, Piece piece) {
        this.board[square.getRank()][square.getFile()] = piece;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withPiece(int file, int rank, Piece piece) {
        board[file][rank] = piece;
        return null;
    }

    @Override
    public AbstractPositionBuilder<T> withWhiteTurn(boolean whiteTurn) {
        this.whiteTurn = whiteTurn;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withEnPassantSquare(int file, int rank) {
        this.enPassantSquare = 1L << (rank * 8 + file);
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        this.castlingBlackKingAllowed = castlingBlackKingAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withHalfMoveClock(int halfMoveClock) {
        this.halfMoveClock = halfMoveClock;
        return this;
    }

    @Override
    public AbstractPositionBuilder<T> withFullMoveClock(int fullMoveClock) {
        this.fullMoveClock = fullMoveClock;
        return this;
    }

}
