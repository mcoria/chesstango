package movegenerators;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Set;

import chess.BoardState;
import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class ReyNegroMoveGenerator extends ReyAbstractMoveGenerator {
	
	protected static final Map.Entry<Square, Pieza> TORRE_NEGRA_REYNA_SQUARE = new SimpleImmutableEntry<Square, Pieza>(Square.a8, Pieza.TORRE_NEGRO);
	protected static final Square DESTINO_ENROQUE_REYNA_SQUARE = Square.c8;
	protected static final Square INTERMEDIO_ENROQUE_REYNA_SQUARE = Square.d8;
	
	protected static final Square REY_NEGRO_SQUARE = Square.e8;
	
	protected static final Square INTERMEDIO_ENROQUE_REY_SQUARE = Square.f8;	
	protected static final Square DESTINO_ENROQUE_REY_SQUARE = Square.g8;
	protected static final Map.Entry<Square, Pieza> TORRE_NEGRA_REY_SQUARE = new SimpleImmutableEntry<Square, Pieza>(Square.h8, Pieza.TORRE_NEGRO);
	
	
	public ReyNegroMoveGenerator() {
		super(Color.NEGRO);
	}
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		assert (Pieza.REY_NEGRO.equals(origen.getValue()));
		Set<Move> moves = getPseudoMoves(dummyBoard, origen);
		if (boardState.isEnroqueNegroReinaPermitido() && puedeEnroque(dummyBoard, origen, REY_NEGRO_SQUARE,
				INTERMEDIO_ENROQUE_REYNA_SQUARE, DESTINO_ENROQUE_REYNA_SQUARE, TORRE_NEGRA_REYNA_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_NEGRO_REYNA);
		}
		
		if (boardState.isEnroqueNegroReyPermitido() && puedeEnroque(dummyBoard, origen, REY_NEGRO_SQUARE,
				INTERMEDIO_ENROQUE_REY_SQUARE, DESTINO_ENROQUE_REY_SQUARE, TORRE_NEGRA_REY_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_NEGRO_REY);
		}		

		return moves;
	}


}
