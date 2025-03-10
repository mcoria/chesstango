package net.chesstango.board.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;

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
	protected Square getOneSquareForward(Square square) {
		return Square.getSquare(square.getFile(), square.getRank() + 1);
	}

	@Override
	protected Square getTwoSquareForward(Square square) {
		return square.getRank() == 1 ? Square.getSquare(square.getFile(), 3) : null;
	}

	@Override
	protected Square getAttackSquareLeft(Square square) {
		return Square.getSquare(square.getFile() - 1, square.getRank() + 1);
	}
	
	@Override
	protected Square getAttackSquareRight(Square square) {
		return Square.getSquare(square.getFile() + 1, square.getRank() + 1);
	}

	@Override
	protected Piece[] getPromotionPieces() {
		return PROMOCIONES_WHITE;
	}

	@Override
	protected Cardinal getDiagonalLeftDirection() {
		return Cardinal.NorteOeste;
	}

	@Override
	protected Cardinal getDiagonalRightDirection() {
		return Cardinal.NorteEste;
	}
}
