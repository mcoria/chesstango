package chess.pseudomovesgenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PawnNegroMoveGenerator extends PawnAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_BLACK = new Pieza[]{Pieza.ROOK_BLACK, Pieza.KNIGHT_BLACK, Pieza.BISHOP_BLACK, Pieza.QUEEN_BLACK};
	
	public PawnNegroMoveGenerator() {
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
	protected PosicionPieza getCapturaPawnPasante(Square peonPasanteSquare) {
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 3), Pieza.PAWN_WHITE);
	}


	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_BLACK;
	}	
}
