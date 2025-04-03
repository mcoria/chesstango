package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionBuilder<T> {
	PositionBuilder<T> withTurn(Color turn);

	PositionBuilder<T> withEnPassantSquare(Square enPassantSquare);

	PositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

	PositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

	PositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

	PositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);
	
	PositionBuilder<T> withPiece(Square square, Piece piece);

	PositionBuilder<T> withHalfMoveClock(int halfMoveClock);

	PositionBuilder<T> withFullMoveClock(int fullMoveClock);
	
	T getChessRepresentation();
}