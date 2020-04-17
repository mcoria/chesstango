package moveexecutors;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

public class EnroqueBlancoReynaMove extends EnroqueMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
	public static final PosicionPieza TO = new PosicionPieza(Square.c1, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.a1, Pieza.TORRE_BLANCO);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.d1, null);
	
	private static final SimpleReyMove REY_MOVE = new SimpleReyMove(FROM, TO);
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
	public PosicionPieza getFrom() {
		return FROM;
	}

	@Override
	public PosicionPieza getTo() {
		return TO;
	}

	@Override
	protected SimpleReyMove getReyMove() {
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
