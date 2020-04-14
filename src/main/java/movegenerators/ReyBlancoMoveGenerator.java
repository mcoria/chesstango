package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.BoardState;
import chess.CachePosiciones;
import chess.Color;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;

public class ReyBlancoMoveGenerator extends ReyAbstractMoveGenerator {

	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b1;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c1;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d1;
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f1;	
	protected static final Square DESTINO_REY_SQUARE = Square.g1;
	
	
	public ReyBlancoMoveGenerator() {
		super(Color.BLANCO);
	}
	
	@Override
	public void generateMoves(Map.Entry<Square, Pieza> origen, Collection<Move> moveContainer) {
		//assert (Pieza.REY_BLANCO.equals(origen.getValue()));
		
		super.generateMoves(origen, moveContainer);
		
		BoardState boardState = this.tablero.getBoardState();
		
		if (boardState.isEnroqueBlancoReinaPermitido() && 
			puedeEnroqueReina(	this.tablero, 
								origen, 
								CachePosiciones.REY_BLANCO, 
								CachePosiciones.TORRE_BLANCA_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			this.filter.filterMove(moveContainer, new EnroqueBlancoReynaMove());
		}
		
		
		if (boardState.isEnroqueBlancoReyPermitido() && 
			puedeEnroqueRey(this.tablero, 
							origen, 
							CachePosiciones.REY_BLANCO, 
							CachePosiciones.TORRE_BLANCA_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {			
			this.filter.filterMove(moveContainer, new EnroqueBlancoReyMove());
		}
	}

}
