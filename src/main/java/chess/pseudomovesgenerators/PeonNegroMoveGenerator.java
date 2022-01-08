package chess.pseudomovesgenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PeonNegroMoveGenerator extends PeonAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_BLACK = new Pieza[]{Pieza.TORRE_BLACK, Pieza.CABALLO_BLACK, Pieza.ALFIL_BLACK, Pieza.QUEEN_BLACK};
	
	public PeonNegroMoveGenerator() {
		super(Color.BLACK);
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

	@Override
	protected PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare) {
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 3), Pieza.PEON_WHITE);
	}


	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_BLACK;
	}	
}
