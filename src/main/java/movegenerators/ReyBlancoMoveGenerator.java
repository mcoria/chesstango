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

public class ReyBlancoMoveGenerator extends ReyAbstractMoveGenerator {
	
	protected static final Map.Entry<Square, Pieza> TORRE_BLANCA_REYNA_SQUARE = new SimpleImmutableEntry<Square, Pieza>(Square.a1, Pieza.TORRE_BLANCO);
	protected static final Square DESTINO_ENROQUE_REYNA_SQUARE = Square.c1;
	protected static final Square INTERMEDIO_ENROQUE_REYNA_SQUARE = Square.d1;
	
	protected static final Square REY_BLANCO_SQUARE = Square.e1;
	
	protected static final Square INTERMEDIO_ENROQUE_REY_SQUARE = Square.f1;	
	protected static final Square DESTINO_ENROQUE_REY_SQUARE = Square.g1;
	protected static final Map.Entry<Square, Pieza> TORRE_BLANCA_REY_SQUARE = new SimpleImmutableEntry<Square, Pieza>(Square.h1, Pieza.TORRE_BLANCO);
	
	
	public ReyBlancoMoveGenerator() {
		super(Color.BLANCO);
	}
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		Set<Move> moves = getPseudoMoves(dummyBoard, origen);
		if (boardState.isEnroqueBlancoReinaPermitido() && puedeEnroque(dummyBoard, origen, REY_BLANCO_SQUARE,
				INTERMEDIO_ENROQUE_REYNA_SQUARE, DESTINO_ENROQUE_REYNA_SQUARE, TORRE_BLANCA_REYNA_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_BLANCO_REINA);
		}
		
		if (boardState.isEnroqueBlancoReyPermitido() && puedeEnroque(dummyBoard, origen, REY_BLANCO_SQUARE,
				INTERMEDIO_ENROQUE_REY_SQUARE, DESTINO_ENROQUE_REY_SQUARE, TORRE_BLANCA_REY_SQUARE)) {
			moves.add(Move.MOVE_ENROQUE_BLANCO_REY);
		}		

		return moves;
	}

}
