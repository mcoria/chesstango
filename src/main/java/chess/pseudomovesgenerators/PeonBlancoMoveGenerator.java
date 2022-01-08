package chess.pseudomovesgenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PeonBlancoMoveGenerator extends PeonAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_WHITE = new Pieza[]{Pieza.TORRE_WHITE, Pieza.CABALLO_WHITE, Pieza.ALFIL_WHITE, Pieza.QUEEN_WHITE};
	
	public PeonBlancoMoveGenerator() {
		super(Color.WHITE);
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
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 4), Pieza.PEON_BLACK);
	}

	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_WHITE;
	}

}
