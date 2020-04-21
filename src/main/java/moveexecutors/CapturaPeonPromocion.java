package moveexecutors;

import chess.DummyBoard;
import chess.Pieza;
import chess.PosicionPieza;

public class CapturaPeonPromocion extends CaptureMove {

	protected final Pieza promocion;
	
	public CapturaPeonPromocion(PosicionPieza from, PosicionPieza to, Pieza promocion) {
		super(from, to);
		this.promocion = promocion;
	}

	@Override
	public void executeMove(DummyBoard board) {
		board.setEmptyPosicion(from);								//Dejamos el origen
		board.setPieza(to.getKey(), this.promocion) ;			//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof CaptureMove){
			CapturaPeonPromocion other = (CapturaPeonPromocion) obj;
			return promocion.equals(other.promocion);
		}
		return false;
	}
	
	@Override
	protected String getType() {
		return "CapturaPeonPromocion";
	}
	
	@Override
	public String toString() {
		return super.toString() + "[" + promocion + "]";
	}	

}
