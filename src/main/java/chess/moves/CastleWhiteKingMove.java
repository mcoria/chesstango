package chess.moves;

import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastleWhiteKingMove extends CastleMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e1, Pieza.REY_BLANCO);
	public static final PosicionPieza TO = new PosicionPieza(Square.g1, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.h1, Pieza.TORRE_BLANCO);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.f1, null);
	
	private static final SimpleKingMove REY_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public CastleWhiteKingMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setCastleWhiteKingPermitido(false);
		boardState.setCastleWhiteReinaPermitido(false);
	}
	
	public String getType() {
		return "CastleWhiteKingMove";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastleWhiteKingMove){
			return true;
		}
		return false;
	}

}