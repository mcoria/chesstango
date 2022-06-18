package chess.board.builder;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;

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