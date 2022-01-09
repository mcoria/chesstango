package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.layers.ChessPositionState;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingWhiteQueenMove extends CastlingMove {

	public static final PiecePositioned FROM = new PiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned TO = new PiecePositioned(Square.c1, null);
	
	public static final PiecePositioned ROOK_FROM = new PiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_TO = new PiecePositioned(Square.d1, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO);
	
	public CastlingWhiteQueenMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(ChessPositionState chessPositionState) {
		super.executeMove(chessPositionState);
		chessPositionState.setCastlingWhiteKingPermitido(false);
		chessPositionState.setCastlingWhiteQueenPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingWhiteQueenMove){
			return true;
		}
		return false;
	}	

}
