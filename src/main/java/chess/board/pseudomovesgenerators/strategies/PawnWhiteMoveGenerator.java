package chess.board.pseudomovesgenerators.strategies;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public class PawnWhiteMoveGenerator extends AbstractPawnMoveGenerator {
	
	private static final Piece[] PROMOCIONES_WHITE = new Piece[]{Piece.ROOK_WHITE, Piece.KNIGHT_WHITE, Piece.BISHOP_WHITE, Piece.QUEEN_WHITE};
	
	public PawnWhiteMoveGenerator() {
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
	protected PiecePositioned getCapturaEnPassant(Square pawnPasanteSquare) {
		return PiecePositioned.getPiecePositioned(Square.getSquare(pawnPasanteSquare.getFile(), 4), Piece.PAWN_BLACK);
	}

	@Override
	protected Piece[] getPiezaPromocion() {
		return PROMOCIONES_WHITE;
	}

}
