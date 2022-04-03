package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class CastlingBlackKingMove extends AbstractCastlingMove {

	public static final PiecePositioned FROM =  PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
	public static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.g8, null);
	
	public static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f8, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO);
	
	public CastlingBlackKingMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setCastlingBlackKingAllowed(false);
		positionState.setCastlingBlackQueenAllowed(false);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof CastlingBlackKingMove){
			return true;
		}
		return false;
	}
}
