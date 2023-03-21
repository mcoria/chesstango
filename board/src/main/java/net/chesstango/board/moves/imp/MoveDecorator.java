package net.chesstango.board.moves.imp;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.PiecePlacementWriter;
import net.chesstango.board.position.PositionStateReader;
import net.chesstango.board.position.imp.ColorBoard;
import net.chesstango.board.position.imp.MoveCacheBoard;
import net.chesstango.board.position.imp.PositionState;
import net.chesstango.board.position.imp.ZobristHash;

/**
 * @author Mauricio Coria
 *
 */
public abstract class MoveDecorator<T extends Move> implements Move {
	
	protected final T move;
	
	public MoveDecorator(T move) {
		this.move = move;
	}	

	@Override
	public PiecePositioned getFrom() {
		return move.getFrom();
	}

	@Override
	public PiecePositioned getTo() {
		return move.getTo();
	}

	@Override
	public void executeMove(PiecePlacementWriter board) {
		move.executeMove(board);
	}

	@Override
	public void undoMove(PiecePlacementWriter board) {
		move.undoMove(board);
	}

	@Override
	public void executeMove(PositionState positionState) {
		move.executeMove(positionState);
	}

	@Override
	public void undoMove(PositionState positionState) {
		move.undoMove(positionState);
	}

	@Override
	public void executeMove(ColorBoard coloBoard) {
		move.executeMove(coloBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		move.undoMove(colorBoard);
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		move.executeMove(moveCache);
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		move.undoMove(moveCache);
	}

	@Override
	public void executeMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
		move.executeMove(hash, oldPositionState, newPositionState);
	}

	@Override
	public void undoMove(ZobristHash hash, PositionStateReader oldPositionState, PositionStateReader newPositionState) {
		executeMove(hash, oldPositionState, newPositionState);
	}
	
	@Override
	public int hashCode() {
		return move.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MoveDecorator){
			return move.equals( ((MoveDecorator) obj).move );
		}
		return false;
	}	
	
	@Override
	public int compareTo(Move theOther) {
		return move.compareTo(theOther);
	}
	
	@Override
	public String toString() {
		return move.toString() + " with " + this.getClass().getSimpleName();
	}

	@Override
	public Cardinal getMoveDirection() {
		return move.getMoveDirection();
	}

}
