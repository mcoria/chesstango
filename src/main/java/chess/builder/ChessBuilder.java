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

	void withPeonPasanteSquare(Square peonPasanteSquare);

	void withCastleWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido);

	void withCastleWhiteKingPermitido(boolean enroqueBlancoKingPermitido);

	void withCastleBlackReinaPermitido(boolean enroqueNegroReinaPermitido);

	void withCastleBlackKingPermitido(boolean enroqueNegroKingPermitido);
	
	void withPieza(Square square, Pieza pieza);	

}