package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.moves.MovePromotion;
import chess.board.position.PiecePlacementWriter;


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
