package chess.builder;

import chess.Color;
import chess.Piece;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionBuilder {

	void withTurno(Color turno);

	void withPawnPasanteSquare(Square peonPasanteSquare);

	void withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed);

	void withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed);

	void withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed);

	void withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed);
	
	void withPieza(Square square, Piece piece);	

}