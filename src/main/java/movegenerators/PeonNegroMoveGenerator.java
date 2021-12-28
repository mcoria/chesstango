package movegenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PeonNegroMoveGenerator extends PeonAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_NEGRO = new Pieza[]{Pieza.TORRE_NEGRO, Pieza.CABALLO_NEGRO, Pieza.ALFIL_NEGRO, Pieza.REINA_NEGRO};
	
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

	@Override
	protected PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare) {
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 3), Pieza.PEON_BLANCO);
	}


	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_NEGRO;
	}	
}
