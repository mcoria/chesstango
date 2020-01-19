package movegenerators;

import java.util.Map;
import java.util.Set;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class ReyNegroMoveGenerator extends ReyAbstractMoveGenerator {
	
	
	protected static final Square INTERMEDIO_TORRE_REYNA_SQUARE = Square.b8;
	protected static final Square DESTINO_REYNA_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_REY_REYNA_SQUARE = Square.d8;
	
	
	
	protected static final Square INTERMEDIO_REY_REY_SQUARE = Square.f8;	
	protected static final Square DESTINO_REY_SQUARE = Square.g8;
	
	
	public ReyNegroMoveGenerator() {
		super(Color.NEGRO);
	}
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		assert (Pieza.REY_NEGRO.equals(origen.getValue()));
		
		Set<Move> moves = getPseudoMoves(dummyBoard, origen);
		
		if (boardState.isEnroqueNegroReinaPermitido() && 
			puedeEnroqueReina(	dummyBoard, 
								origen, 
								DummyBoard.REY_NEGRO, 
								DummyBoard.TORRE_NEGRO_REYNA,
								INTERMEDIO_TORRE_REYNA_SQUARE, 
								DESTINO_REYNA_SQUARE, 
								INTERMEDIO_REY_REYNA_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_NEGRO_REYNA);
		}
			
			
		if (boardState.isEnroqueNegroReyPermitido() && 
			puedeEnroqueRey(dummyBoard, 
							origen, 
							DummyBoard.REY_NEGRO, 
							DummyBoard.TORRE_NEGRO_REY,
							DESTINO_REY_SQUARE, 
							INTERMEDIO_REY_REY_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_NEGRO_REY);
		}

		return moves;
	}


}
