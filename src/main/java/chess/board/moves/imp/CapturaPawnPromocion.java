package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.moves.MovePromotion;
import chess.board.position.PiecePlacementWriter;

/**
 * @author Mauricio Coria
 *
 */
class CapturaPawnPromocion extends CaptureMove implements MovePromotion {

	protected final Piece promocion;
	
	public CapturaPawnPromocion(PiecePositioned from, PiecePositioned to, Piece promocion) {
		super(from, to);
		this.promocion = promocion;
	}

	@Override
	public void executeMove(PiecePlacementWriter board) {
		board.setEmptyPosicion(from);								//Dejamos el origen
		board.setPieza(to.getKey(), this.promocion) ;				//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			CapturaPawnPromocion other = (CapturaPawnPromocion) obj;
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
