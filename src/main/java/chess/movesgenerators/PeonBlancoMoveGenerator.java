package chess.movesgenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PeonBlancoMoveGenerator extends PeonAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_BLANCO = new Pieza[]{Pieza.TORRE_BLANCO, Pieza.CABALLO_BLANCO, Pieza.ALFIL_BLANCO, Pieza.REINA_BLANCO};
	
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

	@Override
	protected PosicionPieza getCapturaPeonPasante(Square peonPasanteSquare) {
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 4), Pieza.PEON_NEGRO);
	}

	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_BLANCO;
	}

}
