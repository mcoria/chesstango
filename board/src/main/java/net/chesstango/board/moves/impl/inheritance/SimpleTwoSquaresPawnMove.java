package net.chesstango.board.moves.impl.inheritance;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;


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
	protected void updatePositionStateBeforeRollTurn(PositionState positionState) {
		super.updatePositionStateBeforeRollTurn(positionState);
		positionState.setEnPassantSquare(enPassantSquare);
	}
	
	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();		
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), enPassantSquare, true);
	}
	
	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), enPassantSquare, false);
		moveCache.popCleared();
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimpleTwoSquaresPawnMove) {
			SimpleTwoSquaresPawnMove other = (SimpleTwoSquaresPawnMove) obj;
			return enPassantSquare.equals(other.enPassantSquare);
		}
		return false;
    }
}