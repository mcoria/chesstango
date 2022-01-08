package chess.moves;

import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastleBlackKingMove extends CastleMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e8, Pieza.REY_NEGRO);
	public static final PosicionPieza TO = new PosicionPieza(Square.g8, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.h8, Pieza.TORRE_NEGRO);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.f8, null);
	
	private static final SimpleKingMove REY_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public CastleBlackKingMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setCastleBlackKingPermitido(false);
		boardState.setCastleBlackReinaPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastleBlackKingMove){
			return true;
		}
		return false;
	}
}