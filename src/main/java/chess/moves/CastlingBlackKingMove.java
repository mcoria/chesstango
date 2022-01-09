package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.layers.ChessPositionState;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingBlackKingMove extends CastlingMove {

	public static final PiecePositioned FROM = new PiecePositioned(Square.e8, Piece.KING_BLACK);
	public static final PiecePositioned TO = new PiecePositioned(Square.g8, null);
	
	public static final PiecePositioned ROOK_FROM = new PiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned ROOK_TO = new PiecePositioned(Square.f8, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO);
	
	public CastlingBlackKingMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(ChessPositionState chessPositionState) {
		super.executeMove(chessPositionState);
		chessPositionState.setCastlingBlackKingPermitido(false);
		chessPositionState.setCastlingBlackQueenPermitido(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingBlackKingMove){
			return true;
		}
		return false;
	}
}
