package movegenerators;

import chess.Color;
import chess.Square;

public class PeonBlancoMoveGenerator extends PeonAbstractMoveGenerator {
	
	public PeonBlancoMoveGenerator() {
		super(Color.BLANCO);
	}
		
	@Override
	protected Square getCasilleroSaltoSimple(Square casillero) {
		return Square.getSquare(casillero.getFile(), casillero.getRank() + 1);
	}

	@Override
	protected Square getCasilleroSaltoDoble(Square casillero) {
		return casillero.getRank() == 1 ? Square.getSquare(casillero.getFile(), 3) : null;
	}

	@Override
	protected Square getCasilleroAtaqueIzquirda(Square casillero) {
		return Square.getSquare(casillero.getFile() - 1, casillero.getRank() + 1);
	}
	
	@Override
	protected Square getCasilleroAtaqueDerecha(Square casillero) {
		return Square.getSquare(casillero.getFile() + 1, casillero.getRank() + 1);
	}

}
