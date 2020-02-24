package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
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
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen) {
		//assert (Pieza.REY_NEGRO.equals(origen.getValue()));
		
		Collection<Move> moves = super.generateMoves(origen);
		
		BoardState boardState = this.tablero.getBoardState();
		
		if (boardState.isEnroqueNegroReinaPermitido() && 
			puedeEnroqueReina(	this.tablero, 
								origen, 
								DummyBoard.REY_NEGRO, 
								DummyBoard.TORRE_NEGRO_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			this.filter.filterMove(moves, new EnroqueNegroReynaMove());
		}
			
			
		if (boardState.isEnroqueNegroReyPermitido() && 
			puedeEnroqueRey(this.tablero, 
							origen, 
							DummyBoard.REY_NEGRO, 
							DummyBoard.TORRE_NEGRO_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {
			this.filter.filterMove(moves, new EnroqueNegroReyMove());
		}

		return moves;
	}


}
