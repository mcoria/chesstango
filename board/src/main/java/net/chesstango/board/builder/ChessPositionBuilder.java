package net.chesstango.board.builder;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionBuilder<T> {

	ChessPositionBuilder<T> withTurn(Color turn);

	ChessPositionBuilder<T> withEnPassantSquare(Square enPassantSquare);

	ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

	ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

	ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

	ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);
	
	ChessPositionBuilder<T> withPiece(Square square, Piece piece);

	ChessPositionBuilder<T> withHalfMoveClock(int halfMoveClock);

	ChessPositionBuilder<T> withFullMoveClock(int fullMoveClock);
	
	T getResult();

}