package chess.moves;

import chess.Pieza;
import chess.PosicionPieza;
import chess.layers.PosicionPiezaBoard;

/**
 * @author Mauricio Coria
 *
 */
class CapturaPawnPromocion extends CaptureMove {

	protected final Pieza promocion;
	
	public CapturaPawnPromocion(PosicionPieza from, PosicionPieza to, Pieza promocion) {
		super(from, to);
		this.promocion = promocion;
	}

	@Override
	public void executeMove(PosicionPiezaBoard board) {
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
