package chess.pseudomovesgenerators;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlancoMoveGenerator extends PawnAbstractMoveGenerator {
	
	private static final Pieza[] PROMOCIONES_WHITE = new Pieza[]{Pieza.ROOK_WHITE, Pieza.KNIGHT_WHITE, Pieza.BISHOP_WHITE, Pieza.QUEEN_WHITE};
	
	public PawnBlancoMoveGenerator() {
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
	protected PosicionPieza getCapturaPawnPasante(Square peonPasanteSquare) {
		return new PosicionPieza(Square.getSquare(peonPasanteSquare.getFile(), 4), Pieza.PAWN_BLACK);
	}

	@Override
	protected Pieza[] getPiezaPromocion() {
		return PROMOCIONES_WHITE;
	}

}
