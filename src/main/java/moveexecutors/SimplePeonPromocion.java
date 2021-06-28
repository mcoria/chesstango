package moveexecutors;

import chess.Pieza;
import chess.PosicionPieza;
import layers.PosicionPiezaBoard;

public class SimplePeonPromocion extends SimpleMove {

	protected final Pieza promocion;
	
	public SimplePeonPromocion(PosicionPieza from, PosicionPieza to, Pieza promocion) {
		super(from, to);
		this.promocion = promocion;
	}

	@Override
	public void executeMove(PosicionPiezaBoard board) {
		board.setEmptyPosicion(from);								//Dejamos el origen
		board.setPieza(to.getKey(), this.promocion) ;			//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimplePeonPromocion){
			SimplePeonPromocion other = (SimplePeonPromocion) obj;
			return promocion.equals(other.promocion);
		}
		return false;
	}
	
	@Override
	public String getType() {
		return "SimplePeonPromocion";
	}

	@Override
	public String toString() {
		return super.toString() + "[" + promocion + "]";
	}	
}
