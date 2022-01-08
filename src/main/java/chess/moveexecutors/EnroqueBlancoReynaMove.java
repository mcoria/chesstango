package chess.moveexecutors;

import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class EnroqueBlancoReynaMove extends EnroqueMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
	public static final PosicionPieza TO = new PosicionPieza(Square.c1, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.a1, Pieza.TORRE_BLANCO);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.d1, null);
	
	private static final SimpleReyMove REY_MOVE = new SimpleReyMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public EnroqueBlancoReynaMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setEnroqueBlancoReyPermitido(false);
		boardState.setEnroqueBlancoReinaPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueBlancoReynaMove){
			return true;
		}
		return false;
	}	

}
