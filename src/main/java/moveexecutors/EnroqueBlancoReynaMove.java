package moveexecutors;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import chess.Square;

public class EnroqueBlancoReynaMove extends EnroqueMove {

	public static final Map.Entry<Square, Pieza> FROM = new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO);
	public static final Map.Entry<Square, Pieza> TO = new SimpleImmutableEntry<Square, Pieza>(Square.c1, null);
	
	public static final Map.Entry<Square, Pieza> TORRE_FROM = new SimpleImmutableEntry<Square, Pieza>(Square.a1, Pieza.TORRE_BLANCO);
	public static final Map.Entry<Square, Pieza> TORRE_TO = new SimpleImmutableEntry<Square, Pieza>(Square.d1, null);
	
	private static final SimpleMove REY_MOVE = new SimpleReyBlancoMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
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
		return "EnroqueBlancoReynaMove";
	}

}
