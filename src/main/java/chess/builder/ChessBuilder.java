package chess.builder;

import chess.Color;
import chess.Pieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessBuilder {

	void withTurno(Color turno);

	void withPawnPasanteSquare(Square peonPasanteSquare);

	void withCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido);

	void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido);

	void withCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido);

	void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido);
	
	void withPieza(Square square, Pieza pieza);	

}