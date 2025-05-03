package net.chesstango.board.representations;

import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * Interface for building a chess position. A chess positions consists of:
 * <ul>
 * <li>Piece placement on the Board Representation</li>
 * <li>Side to move</li>
 * <li>Castling Rights</li>
 * <li>En passant target square</li>
 * <li>Halfmove Clock</li>
 * </ul>
 *
 * @author Mauricio Coria
 */
public interface PositionBuilder<T> {


    /**
     * Sets the turn to indicate whether it is White's turn to move.
     *
     * @param whiteTurn True if it is White's turn to move, false if it is Black's turn.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withWhiteTurn(boolean whiteTurn);


    /**
     * Sets the en passant square, which indicates the target square of a possible en passant capture.
     *
     * @param file The file of the en passant square (0-based index).
     * @param rank The rank of the en passant square (0-based index).
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withEnPassantSquare(int file, int rank);

    /**
     * Sets whether castling with the white queen is allowed.
     *
     * @param castlingWhiteQueenAllowed True if castling with the white queen is allowed, false otherwise.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

    /**
     * Sets whether castling with the white king is allowed.
     *
     * @param castlingWhiteKingAllowed True if castling with the white king is allowed, false otherwise.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

    /**
     * Sets whether castling with the black queen is allowed.
     *
     * @param castlingBlackQueenAllowed True if castling with the black queen is allowed, false otherwise.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

    /**
     * Sets whether castling with the black king is allowed.
     *
     * @param castlingBlackKingAllowed True if castling with the black king is allowed, false otherwise.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);

    /**
     * Places a piece on a specific square.
     *
     * @param square The square where the piece is to be placed.
     * @param piece  The piece to be placed on the square.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withPiece(Square square, Piece piece);


    /**
     * Places a piece on a specific board square determined by the file and rank.
     *
     * @param file The file of the square where the piece is to be placed (0-based index).
     * @param rank The rank of the square where the piece is to be placed (0-based index).
     * @param piece The chess piece to be placed on the specified square.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withPiece(int file, int rank, Piece piece);

    /**
     * Sets the half-move clock.
     *
     * @param halfMoveClock The number of half-moves since the last capture or pawn move.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withHalfMoveClock(int halfMoveClock);

    /**
     * Sets the full-move clock.
     *
     * @param fullMoveClock The number of full moves in the game.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withFullMoveClock(int fullMoveClock);

    /**
     * Gets the chess position representation built by this builder.
     *
     * @return The chess representation.
     */
    T getPositionRepresentation();
}