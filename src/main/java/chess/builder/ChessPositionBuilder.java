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

	void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed);

	void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed);

	void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed);

	void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed);
	
	void withPieza(Square square, Piece piece);	

}