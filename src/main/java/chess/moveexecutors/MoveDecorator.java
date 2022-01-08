package chess.moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.PosicionPieza;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PosicionPiezaBoard;

/**
 * @author Mauricio Coria
 *
 */
abstract class MoveDecorator implements Move {
	
	protected final Move move;	
	
	public MoveDecorator(Move move) {
		this.move = move;
	}	

	@Override
	public PosicionPieza getFrom() {
		return move.getFrom();
	}

	@Override
	public PosicionPieza getTo() {
		return move.getTo();
	}
	
	@Override
	public void executeMove(PosicionPiezaBoard board) {
		move.executeMove(board);
	}

	@Override
	public void undoMove(PosicionPiezaBoard board) {
		move.undoMove(board);
	}

	@Override
	public void executeMove(BoardState boardState) {
		move.executeMove(boardState);
	}

	@Override
	public void undoMove(BoardState boardState) {
		move.undoMove(boardState);
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
	public void executeMove(KingCacheBoard kingCacheBoard){
		move.executeMove(kingCacheBoard);
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		move.undoMove(kingCacheBoard);
	}
	
	@Override
	public void executeMove(Board board) {
		board.executeMove(this);
	}

	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
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
		return move.toString() + " with " + this.getClass().getSimpleName().toString();
	}	

}
