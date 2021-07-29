package moveexecutors;

import chess.BoardState;
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
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setEnroqueNegroReyPermitido(false);
		boardState.setEnroqueNegroReinaPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueNegroReyMove){
			return true;
		}
		return false;
	}
}
