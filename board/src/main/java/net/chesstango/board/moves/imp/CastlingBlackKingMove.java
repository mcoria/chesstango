package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public class CastlingBlackKingMove extends AbstractCastlingMove {

	public static final PiecePositioned FROM =  PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK);
	public static final PiecePositioned TO = PiecePositioned.getPiecePositioned(Square.g8, null);
	
	public static final PiecePositioned ROOK_FROM = PiecePositioned.getPiecePositioned(Square.h8, Piece.ROOK_BLACK);
	public static final PiecePositioned ROOK_TO = PiecePositioned.getPiecePositioned(Square.f8, null);
	
	private static final SimpleKingMove KING_MOVE = new SimpleKingMove(FROM, TO);
	private static final SimpleMove ROOK_MOVE = new SimpleMove(ROOK_FROM, ROOK_TO, Cardinal.Oeste);
	
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
	public void executeMove(ZobristHash hash) {
		super.executeMove(hash);
		hash.xorCastleBlackKing();
	}
	@Override
	public boolean equals(Object obj) {
        return obj instanceof CastlingBlackKingMove;
    }
}
