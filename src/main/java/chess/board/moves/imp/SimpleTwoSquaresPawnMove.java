package chess.board.moves.imp;

import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;
import chess.board.position.imp.MoveCacheBoard;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
class SimpleTwoSquaresPawnMove extends SimplePawnMove {
	
	private final Square enPassantSquare;

	public SimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare, Cardinal cardinal) {
		super(from, to, cardinal);
		this.enPassantSquare = enPassantSquare;
	}
	
	@Override
	public void executeMove(PositionState positionState) {
		super.executeMove(positionState);
		positionState.setEnPassantSquare(enPassantSquare);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();		
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), enPassantSquare, true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getKey(), to.getKey(), enPassantSquare, false);
		moveCache.popCleared();
	}	
	
	@Override
	public boolean equals(Object obj) {
        return super.equals(obj) && obj instanceof SimpleTwoSquaresPawnMove;
    }
}
