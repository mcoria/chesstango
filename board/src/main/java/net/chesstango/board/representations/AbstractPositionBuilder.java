package net.chesstango.board.representations;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;

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

    protected long whitePositions = 0;
    protected long blackPositions = 0;
    protected long kingPositions = 0;
    protected long queenPositions = 0;
    protected long rookPositions = 0;
    protected long bishopPositions = 0;
    protected long knightPositions = 0;
    protected long pawnPositions = 0;


    @Override
    public AbstractPositionBuilder<T> withPiece(int file, int rank, Piece piece) {
        int square = rank * 8 + file;

        if (Color.WHITE.equals(piece.getColor())) {
            whitePositions |= 1L << square;
        } else {
            blackPositions |= 1L << square;
        }

        switch (piece) {
            case KING_WHITE, KING_BLACK -> kingPositions |= 1L << square;
            case QUEEN_WHITE, QUEEN_BLACK -> queenPositions |= 1L << square;
            case ROOK_WHITE, ROOK_BLACK -> rookPositions |= 1L << square;
            case BISHOP_WHITE, BISHOP_BLACK -> bishopPositions |= 1L << square;
            case KNIGHT_WHITE, KNIGHT_BLACK -> knightPositions |= 1L << square;
            case PAWN_WHITE, PAWN_BLACK -> pawnPositions |= 1L << square;
        }

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
