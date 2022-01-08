package chess.moves;

import chess.BoardState;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingBlackQueenMove extends CastlingMove {

	public static final PosicionPieza FROM = new PosicionPieza(Square.e8, Pieza.KING_BLACK);
	public static final PosicionPieza TO = new PosicionPieza(Square.c8, null);
	
	public static final PosicionPieza TORRE_FROM = new PosicionPieza(Square.a8, Pieza.TORRE_BLACK);
	public static final PosicionPieza TORRE_TO = new PosicionPieza(Square.d8, null);
	
	private static final SimpleKingMove REY_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove TORRE_MOVE = new SimpleMove(TORRE_FROM, TORRE_TO);
	
	public CastlingBlackQueenMove() {
		super(REY_MOVE, TORRE_MOVE);
	}
	
	
	@Override
	public void executeMove(BoardState boardState) {
		super.executeMove(boardState);
		boardState.setCastlingBlackKingPermitido(false);
		boardState.setCastlingBlackReinaPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingBlackQueenMove){
			return true;
		}
		return false;
	}
}
