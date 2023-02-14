package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessRepresentationBuilder<T> {
	ChessRepresentationBuilder<T> withTurn(Color turn);

	ChessRepresentationBuilder<T> withEnPassantSquare(Square enPassantSquare);

	ChessRepresentationBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

	ChessRepresentationBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

	ChessRepresentationBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

	ChessRepresentationBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);
	
	ChessRepresentationBuilder<T> withPiece(Square square, Piece piece);

	ChessRepresentationBuilder<T> withHalfMoveClock(int halfMoveClock);

	ChessRepresentationBuilder<T> withFullMoveClock(int fullMoveClock);
	
	T getResult();
}