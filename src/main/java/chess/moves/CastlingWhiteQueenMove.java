package chess.moves;

import chess.BoardState;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingWhiteQueenMove extends CastlingMove {

	public static final PiecePositioned FROM = new PiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned TO = new PiecePositioned(Square.c1, null);
	
	public static final PiecePositioned TORRE_FROM = new PiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned TORRE_TO = new PiecePositioned(Square.d1, null);
	
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
