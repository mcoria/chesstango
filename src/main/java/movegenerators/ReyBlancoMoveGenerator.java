package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

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
	public Collection<Move> generateMoves(Map.Entry<Square, Pieza> origen) {
		assert (Pieza.REY_BLANCO.equals(origen.getValue()));
		Collection<Move> moves = super.generateMoves(origen);
		
		BoardState boardState = this.tablero.getBoardState();
		
		if (boardState.isEnroqueBlancoReinaPermitido() && 
			puedeEnroqueReina(	this.tablero, 
								origen, 
								DummyBoard.REY_BLANCO, 
								DummyBoard.TORRE_BLANCA_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			this.filter.filterMove(moves, Move.MOVE_ENROQUE_BLANCO_REINA);
		}
		
		
		if (boardState.isEnroqueBlancoReyPermitido() && 
			puedeEnroqueRey(this.tablero, 
							origen, 
							DummyBoard.REY_BLANCO, 
							DummyBoard.TORRE_BLANCA_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {			
			this.filter.filterMove(moves, Move.MOVE_ENROQUE_BLANCO_REY);
		}
		

		return moves;
	}

}
