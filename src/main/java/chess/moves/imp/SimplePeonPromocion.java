package chess.moves.imp;

import chess.Piece;
import chess.PiecePositioned;
import chess.position.PiecePlacementWritter;


/**
 * @author Mauricio Coria
 *
 */
class SimplePawnPromocion extends SimpleMove {

	protected final Piece promocion;
	
	public SimplePawnPromocion(PiecePositioned from, PiecePositioned to, Piece promocion) {
		super(from, to);
		this.promocion = promocion;
	}

	@Override
	public void executeMove(PiecePlacementWritter board) {
		board.setEmptyPosicion(from);								//Dejamos el origen
		board.setPieza(to.getKey(), this.promocion) ;				//Promocion
	}
	
	@Override
	public boolean equals(Object obj) {
		if(super.equals(obj)  && obj instanceof SimplePawnPromocion){
			SimplePawnPromocion other = (SimplePawnPromocion) obj;
			return promocion.equals(other.promocion);
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + "[" + promocion + "]";
	}	
}
