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

	void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido);

	void withEnroqueBlancoKingPermitido(boolean enroqueBlancoKingPermitido);

	void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido);

	void withEnroqueNegroKingPermitido(boolean enroqueNegroKingPermitido);
	
	void withPieza(Square square, Pieza pieza);	

}