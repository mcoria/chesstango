package chess.moves.imp;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class CastlingWhiteKingMove extends AbstractCastlingMove {

	public static final PiecePositioned FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.g1, null);
	
	public static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f1, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO);
	
	public CastlingWhiteKingMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setCastlingWhiteKingAllowed(false);
		positionState.setCastlingWhiteQueenAllowed(false);
	}
	
	public String getType() {
		return "CastlingWhiteKingMove";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingWhiteKingMove){
			return true;
		}
		return false;
	}

}
