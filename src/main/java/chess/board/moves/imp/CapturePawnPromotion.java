package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.moves.MovePromotion;
import chess.board.position.PiecePlacementWriter;

/**
 * @author Mauricio Coria
 *
 */
class CapturePawnPromotion extends CaptureMove implements MovePromotion {

	protected final Piece promocion;
	
	public CapturePawnPromotion(PiecePositioned from, PiecePositioned to, Piece promotion) {
		super(from, to);
		this.promocion = promotion;
	}

	@Override
	public void executeMove(PiecePlacementWriter board) {
		board.setEmptyPosicion(from);								//Dejamos el origen
		board.setPieza(to.getKey(), this.promocion) ;				//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			CapturePawnPromotion other = (CapturePawnPromotion) obj;
			return promocion.equals(other.promocion);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + promocion + "]";
	}	

	@Override
	public Piece getPromotion() {
		return promocion;
	}	
}
