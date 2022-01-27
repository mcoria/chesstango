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

	ChessPositionBuilder<T> withPawnPasanteSquare(Square peonPasanteSquare);

	ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed);

	ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed);

	ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed);

	ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed);
	
	ChessPositionBuilder<T> withPieza(Square square, Piece piece);
	
	
	T getResult();

}