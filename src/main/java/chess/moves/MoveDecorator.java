package chess.moves;

import chess.PiecePositioned;
import chess.position.ChessPosition;
import chess.position.ColorBoard;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.position.PositionState;

/**
 * @author Mauricio Coria
 *
 */
abstract class MoveDecorator<T extends Move> implements Move {
	
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
	public void executeMove(PiecePlacement board) {
		move.executeMove(board);
	}

	@Override
	public void undoMove(PiecePlacement board) {
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
	public void executeMove(ChessPosition chessPosition) {
		chessPosition.executeMove(this);
	}

	@Override
	public void undoMove(ChessPosition chessPosition) {
		chessPosition.undoMove(this);
	}	
	
	@Override
	public int hashCode() {
		return move.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof MoveDecorator){
			return move.equals( ((MoveDecorator<?>) obj).move );
		}
		return false;
	}	
	
	@Override
	public int compareTo(Move theOther) {
		return move.compareTo(theOther);
	}
	
	@Override
	public String toString() {
		return move.toString() + " with " + this.getClass().getSimpleName().toString();
	}	

}
