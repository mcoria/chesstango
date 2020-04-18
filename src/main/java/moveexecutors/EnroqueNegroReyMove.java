package moveexecutors;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

public class EnroqueNegroReyMove extends EnroqueMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e8, Pieza.REY_NEGRO);
	public static final PosicionPieza TO = new PosicionPieza(Square.g8, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.h8, Pieza.TORRE_NEGRO);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.f8, null);
	
	private static final SimpleReyMove REY_MOVE = new SimpleReyMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public EnroqueNegroReyMove() {
		super(FROM, TO);
	}
	
	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
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
		return "EnroqueNegroReyMove";
	}	

}
