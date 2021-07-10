package builder;

import chess.Color;
import chess.Pieza;
import chess.Square;

public interface ChessBuilder {

	void withTurno(Color turno);

	void withPeonPasanteSquare(Square peonPasanteSquare);

	void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido);

	void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido);

	void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido);

	void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido);
	
	void withPieza(Square square, Pieza pieza);	

}