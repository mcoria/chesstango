package chess.builder;

import chess.Color;
import chess.Piece;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessBuilder {

	void withTurno(Color turno);

	void withPawnPasanteSquare(Square peonPasanteSquare);

	void withCastlingWhiteQueenPermitido(boolean enroqueBlancoQueenPermitido);

	void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido);

	void withCastlingBlackQueenPermitido(boolean enroqueNegroQueenPermitido);

	void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido);
	
	void withPieza(Square square, Piece piece);	

}