package net.chesstango.board.representations;

import net.chesstango.board.Color;
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
     * Sets the turn color.
     *
     * @param turn The color of the player to move.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withTurn(Color turn);

    /**
     * Sets the en passant square.
     *
     * @param enPassantSquare The square where en passant is possible.
     * @return The current instance of the PositionBuilder.
     */
    PositionBuilder<T> withEnPassantSquare(Square enPassantSquare);

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