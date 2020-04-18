package movegenerators;

import java.util.Collection;

import chess.CachePosiciones;
import chess.Color;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import moveexecutors.EnroqueNegroReyMove;
import moveexecutors.EnroqueNegroReynaMove;

public class ReyNegroMoveGenerator extends ReyAbstractMoveGenerator {

	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b8;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d8;
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f8;	
	protected static final Square DESTINO_REY_SQUARE = Square.g8;
	
	
	public ReyNegroMoveGenerator() {
		super(Color.NEGRO);
	}
	
	@Override
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer) {
		//assert (Pieza.REY_NEGRO.equals(origen.getValue()));
		
		super.generateMoves(origen, moveContainer);
		
		if (this.boardState.isEnroqueNegroReinaPermitido() && 
			puedeEnroqueReina(	this.tablero, 
								origen, 
								CachePosiciones.REY_NEGRO, 
								CachePosiciones.TORRE_NEGRO_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
	    	Move move = new EnroqueNegroReynaMove();
			if(this.filter.filterMove(move)){
				moveContainer.add(move);
			}			
		}
			
			
		if (this.boardState.isEnroqueNegroReyPermitido() && 
			puedeEnroqueRey(this.tablero, 
							origen, 
							CachePosiciones.REY_NEGRO, 
							CachePosiciones.TORRE_NEGRO_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {
	    	Move move = new EnroqueNegroReyMove();
			if(this.filter.filterMove(move)){
				moveContainer.add(move);
			}			
		}
	}


}
