package net.chesstango.board.builders;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.internal.position.SquareBoardImp;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.PositionBuilder;

/**
 * @author Mauricio Coria
 */
public class SquareBoardBuilder implements PositionBuilder<SquareBoard> {

    private final SquareBoard squareBoard;

    public SquareBoardBuilder() {
        this(new SquareBoardImp());
    }

    SquareBoardBuilder(SquareBoard squareBoard) {
        this.squareBoard = squareBoard;
    }

    @Override
    public SquareBoard getPositionRepresentation() {
        return squareBoard;
    }

    @Override
    public SquareBoardBuilder withWhiteTurn(boolean whiteTurn) {
        return this;
    }

    @Override
    public SquareBoardBuilder withWhiteKing(int file, int rank) {
        return withPiece(file, rank, Piece.KING_WHITE);
    }

    @Override
    public SquareBoardBuilder withWhiteQueen(int file, int rank) {
        return withPiece(file, rank, Piece.QUEEN_WHITE);
    }

    @Override
    public SquareBoardBuilder withWhiteRook(int file, int rank) {
        return withPiece(file, rank, Piece.ROOK_WHITE);
    }

    @Override
    public SquareBoardBuilder withWhiteBishop(int file, int rank) {
        return withPiece(file, rank, Piece.BISHOP_WHITE);
    }

    @Override
    public SquareBoardBuilder withWhiteKnight(int file, int rank) {
        return withPiece(file, rank, Piece.KNIGHT_WHITE);
    }

    @Override
    public SquareBoardBuilder withWhitePawn(int file, int rank) {
        return withPiece(file, rank, Piece.PAWN_WHITE);
    }

    @Override
    public SquareBoardBuilder withBlackKing(int file, int rank) {
        return withPiece(file, rank, Piece.KING_BLACK);
    }

    @Override
    public SquareBoardBuilder withBlackQueen(int file, int rank) {
        return withPiece(file, rank, Piece.QUEEN_BLACK);
    }

    @Override
    public SquareBoardBuilder withBlackRook(int file, int rank) {
        return withPiece(file, rank, Piece.ROOK_BLACK);
    }

    @Override
    public SquareBoardBuilder withBlackBishop(int file, int rank) {
        return withPiece(file, rank, Piece.BISHOP_BLACK);
    }

    @Override
    public SquareBoardBuilder withBlackKnight(int file, int rank) {
        return withPiece(file, rank, Piece.KNIGHT_BLACK);
    }

    @Override
    public SquareBoardBuilder withBlackPawn(int file, int rank) {
        return withPiece(file, rank, Piece.PAWN_BLACK);
    }

    @Override
    public SquareBoardBuilder withEnPassantSquare(int file, int rank) {
        return this;
    }

    @Override
    public SquareBoardBuilder withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
        return this;
    }

    @Override
    public SquareBoardBuilder withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
        return this;
    }

    @Override
    public SquareBoardBuilder withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
        return this;
    }

    @Override
    public SquareBoardBuilder withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
        return this;
    }

    @Override
    public SquareBoardBuilder withHalfMoveClock(int halfMoveClock) {
        return this;
    }

    @Override
    public SquareBoardBuilder withFullMoveClock(int fullMoveClock) {
        return this;
    }


    public SquareBoardBuilder withPiece(int file, int rank, Piece piece) {
        Square square = Square.getSquare(file, rank);
        squareBoard.setPiece(square, piece);
        return this;
    }
}
