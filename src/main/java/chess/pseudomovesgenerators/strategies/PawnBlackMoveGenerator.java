package chess.pseudomovesgenerators.strategies;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PawnBlackMoveGenerator extends AbstractPawnMoveGenerator {
	
	private static final Piece[] PROMOCIONES_BLACK = new Piece[]{Piece.ROOK_BLACK, Piece.KNIGHT_BLACK, Piece.BISHOP_BLACK, Piece.QUEEN_BLACK};
	
	public PawnBlackMoveGenerator() {
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
	protected PiecePositioned getCapturaEnPassant(Square pawnPasanteSquare) {
		return PiecePositioned.getPiecePositioned(Square.getSquare(pawnPasanteSquare.getFile(), 3), Piece.PAWN_WHITE);
	}


	@Override
	protected Piece[] getPiezaPromocion() {
		return PROMOCIONES_BLACK;
	}	
}
