package chess.moves;

import chess.ChessPosition;
import chess.PiecePositioned;
import chess.layers.ChessPositionState;
import chess.layers.ColorBoard;
import chess.layers.KingCacheBoard;
import chess.layers.MoveCacheBoard;
import chess.layers.PiecePlacement;

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
	public void executeMove(ChessPositionState chessPositionState) {
		move.executeMove(chessPositionState);
	}

	@Override
	public void undoMove(ChessPositionState chessPositionState) {
		move.undoMove(chessPositionState);
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
