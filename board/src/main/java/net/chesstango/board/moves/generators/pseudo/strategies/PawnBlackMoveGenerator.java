package net.chesstango.board.moves.generators.pseudo.strategies;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;

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
	protected Square getOneSquareForward(Square square) {
		return Square.getSquare(square.getFile(), square.getRank() - 1);
	}

	@Override
	protected Square getTwoSquareForward(Square square) {
		return square.getRank() == 6 ? Square.getSquare(square.getFile(), 4) : null;
	}

	@Override
	protected Square getAttackSquareLeft(Square square) {
		return Square.getSquare(square.getFile() - 1, square.getRank() - 1);
	}
	
	@Override
	protected Square getAttackSquareRight(Square square) {
		return Square.getSquare(square.getFile() + 1, square.getRank() - 1);
	}

	@Override
	protected Piece[] getPromotionPieces() {
		return PROMOCIONES_BLACK;
	}

	@Override
	protected Cardinal getDiagonalLeftDirection() {
		return Cardinal.SurOeste;
	}

	@Override
	protected Cardinal getDiagonalRightDirection() {
		return Cardinal.SurEste;
	}
}
