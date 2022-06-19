package chess.board.movesgenerators.pseudo.strategies;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.moves.Move;

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

	@Override
	protected Move createSimpleMove(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createSimplePawnMove(origen, destino, Cardinal.Sur);
	}

	@Override
	protected Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero) {
		return this.moveFactory.createSaltoDoblePawnMove(origen, destino, saltoSimpleCasillero, Cardinal.Sur);
	}

	@Override
	protected Move createCaptureMoveIzquierda(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCapturePawnMove(origen, destino, Cardinal.SurOeste);
	}

	@Override
	protected Move createCaptureMoveDerecha(PiecePositioned origen, PiecePositioned destino) {
		return this.moveFactory.createCapturePawnMove(origen, destino, Cardinal.SurEste);
	}
}
