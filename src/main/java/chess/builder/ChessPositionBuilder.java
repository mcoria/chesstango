package chess.builder;

import chess.Color;
import chess.Piece;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionBuilder<T> {

	ChessPositionBuilder<T> withTurno(Color turno);

	ChessPositionBuilder<T> withEnPassantSquare(Square pawnPasanteSquare);

	ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

	ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

	ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

	ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);
	
	ChessPositionBuilder<T> withPieza(Square square, Piece piece);
	
	
	T getResult();

}