package movegenerators;

import chess.Color;
import chess.Square;

public class PeonNegroMoveGenerator extends PeonAbstractMoveGenerator {
	
	public PeonNegroMoveGenerator() {
		super(Color.NEGRO);
	}


	@Override
	protected Square getCasilleroSaltoSimple(Square casillero) {
		return Square.getSquare(casillero.getFile(), casillero.getRank() - 1);
	}

	@Override
	protected Square getCasilleroSaltoDoble(Square casillero) {
		return casillero.getRank() == 6 ? Square.getSquare(casillero.getFile(), 4) : null;
	}

	@Override
	protected Square getCasilleroAtaqueIzquirda(Square casillero) {
		return Square.getSquare(casillero.getFile() - 1, casillero.getRank() - 1);
	}
	
	@Override
	protected Square getCasilleroAtaqueDerecha(Square casillero) {
		return Square.getSquare(casillero.getFile() + 1, casillero.getRank() - 1);
	}	

}
