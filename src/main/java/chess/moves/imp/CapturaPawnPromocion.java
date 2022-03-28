package chess.moves.imp;

import chess.Piece;
import chess.PiecePositioned;
import chess.position.PiecePlacementWriter;

/**
 * @author Mauricio Coria
 *
 */
class CapturaPawnPromocion extends CaptureMove {

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

}
