package chess.board.moves.imp;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
class CastlingWhiteQueenMove extends AbstractCastlingMove {

	public static final PiecePositioned FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.c1, null);
	
	public static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.d1, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO);
	
	public CastlingWhiteQueenMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setCastlingWhiteKingAllowed(false);
		positionState.setCastlingWhiteQueenAllowed(false);
	}
	
	@Override
	public boolean equals(Object obj) {
        return obj instanceof CastlingWhiteQueenMove;
    }

}
