package chess.moves;

import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingWhiteQueenMove extends CastlingMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e1, Pieza.KING_WHITE);
	public static final PosicionPieza TO = new PosicionPieza(Square.c1, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.a1, Pieza.ROOK_WHITE);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.d1, null);
	
	private static final SimpleKingMove REY_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public CastlingWhiteQueenMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setCastlingWhiteKingPermitido(false);
		boardState.setCastlingWhiteQueenPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingWhiteQueenMove){
			return true;
		}
		return false;
	}	

}
