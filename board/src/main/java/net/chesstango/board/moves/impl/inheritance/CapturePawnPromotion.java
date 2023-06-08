package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.position.BoardWriter;

/**
 * @author Mauricio Coria
 *
 */
class CapturePawnPromotion extends CapturePawnMove implements MovePromotion {

	protected final Piece promotion;
	
	public CapturePawnPromotion(PiecePositioned from, PiecePositioned to, Piece promotion) {
		super(from, to);
		this.promotion = promotion;
	}

	@Override
	public void executeMove(BoardWriter board) {
		board.setEmptyPosition(from);								//Dejamos el origen
		board.setPiece(to.getSquare(), this.promotion) ;			//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CapturePawnPromotion){
			CapturePawnPromotion other = (CapturePawnPromotion) obj;
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
