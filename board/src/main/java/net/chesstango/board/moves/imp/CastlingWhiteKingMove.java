package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingWhiteKingMove extends AbstractCastlingMove {

	public static final PiecePositioned FROM = PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE);
	public static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.g1, null);
	
	public static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h1, Piece.ROOK_WHITE);
	public static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f1, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO, Cardinal.Oeste);
	
	public CastlingWhiteKingMove() {
		super(KING_MOVE, ROOK_MOVE);
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setCastlingWhiteKingAllowed(false);
		positionState.setCastlingWhiteQueenAllowed(false);
	}

	@Override
	public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
		super.executeMove(hash, oldPositionState, newPositionState);
		hash.xorCastleWhiteKing();
	}
	
	public String getType() {
		return "CastlingWhiteKingMove";
	}
	
	@Override
	public boolean equals(Object obj) {
        return obj instanceof CastlingWhiteKingMove;
    }

}
