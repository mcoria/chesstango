package net.chesstango.board.moves.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PiecePlacementReader;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
abstract class AbstractMove implements Move {
	protected final PiecePositioned from;
	protected final PiecePositioned to;
	protected final Cardinal direction;

	public AbstractMove(PiecePositioned from, PiecePositioned to, Cardinal direction) {
		this.from = from;
		this.to = to;
		this.direction = direction;
	}

	public AbstractMove(PiecePositioned from, PiecePositioned to) {
		this.from = from;
		this.to = to;
		this.direction =  calculateMoveDirection();
	}	

	@Override
	public PiecePositioned getFrom() {
		return from;
	}

	@Override
	public PiecePositioned getTo() {
		return to;
	}
	
	@Override
	public void executeMove(PiecePlacementWriter board) {
		board.move(from, to);
	}
	
	@Override
	public void undoMove(PiecePlacementWriter board) {
		board.setPosition(to);							//Reestablecemos destino
		board.setPosition(from);						//Volvemos a origen
	}	

	//TODO: implementar un decorator antes de crear el movimiento
	@Override
	public final void executeMove(PositionState positionState) {
		positionState.pushState();
		positionState.incrementFullMoveClock();

		updatePositionStateBeforeRollTurn(positionState);

		positionState.rollTurn();
	}

	protected abstract void updatePositionStateBeforeRollTurn(PositionState positionState);

	@Override
	public void undoMove(PositionState positionState) {
		positionState.popState();		
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushCleared();
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), true);
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.clearPseudoMoves(from.getSquare(), to.getSquare(), false);
		moveCache.popCleared();
	}


	@Override
	public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState, PiecePlacementReader board) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public int hashCode() {
		return from.getSquare().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return from.equals(theOther.getFrom()) &&  to.equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + " - " + this.getClass().getSimpleName();
	}


	@Override
	public Cardinal getMoveDirection() {
		return direction;
	}

	private Cardinal calculateMoveDirection() {
		Piece piece = getFrom().getPiece();
		return Piece.KNIGHT_WHITE.equals(piece) ||
				Piece.KNIGHT_BLACK.equals(piece)
				? null : Cardinal.calculateSquaresDirection(getFrom().getSquare(), getTo().getSquare());
	}

}
