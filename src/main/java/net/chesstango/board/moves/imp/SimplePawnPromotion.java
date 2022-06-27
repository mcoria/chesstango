package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.PiecePlacementWriter;


/**
 * @author Mauricio Coria
 *
 */
class SimplePawnPromotion extends SimplePawnMove implements MovePromotion {

	protected final Piece promotion;

	public SimplePawnPromotion(PiecePositioned from, PiecePositioned to, Piece promotion) {
		super(from, to);
		this.promotion = promotion;
	}

	public SimplePawnPromotion(PiecePositioned from, PiecePositioned to, Cardinal cardinal, Piece promotion) {
		super(from, to, cardinal);
		this.promotion = promotion;
	}

	@Override
	public void executeMove(PiecePlacementWriter board) {
		board.setEmptyPosicion(from); // Dejamos el origen
		board.setPieza(to.getKey(), this.promotion); // Promocion
	}

	@Override
	public boolean equals(Object obj) {
		if (super.equals(obj) && obj instanceof SimplePawnPromotion) {
			SimplePawnPromotion other = (SimplePawnPromotion) obj;
			return promotion.equals(other.promotion);
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + "[" + promotion + "]";
	}

	@Override
	public Piece getPromotion() {
		return promotion;
	}
}
