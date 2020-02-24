package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class EnroqueNegroReynaMove extends EnroqueMove {

	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO);
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.c8, null);
	
	public static final Map.Entry<Square, Pieza> TORRE_FROM = new SimpleImmutableEntry<Square, Pieza>(Square.a8, Pieza.TORRE_NEGRO);
	public static final Map.Entry<Square, Pieza> TORRE_TO = new SimpleImmutableEntry<Square, Pieza>(Square.d8, null);
	
	private static final SimpleMove REY_MOVE = new SimpleMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
		boardState.setPeonPasanteSquare(null);	
		boardState.rollTurno();
	}

	@Override
	public Entry<Square, Pieza> getFrom() {
		return FROM;
	}

	@Override
	public Entry<Square, Pieza> getTo() {
		return TO;
	}

	@Override
	protected Move getReyMove() {
		return REY_MOVE;
	}

	@Override
	protected Move getTorreMove() {
		return TORRE_MOVE;
	}	
	
	@Override
	protected String getType() {
		return "EnroqueNegroReynaMove";
	}	

}
