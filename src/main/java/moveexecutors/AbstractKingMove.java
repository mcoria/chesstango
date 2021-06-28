package moveexecutors;

import chess.BoardState;
import chess.KingMove;
import chess.Move;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

public abstract class AbstractKingMove implements Friendly, KingMove {
	
	protected Move move;
	
	public AbstractKingMove(Move move) {
		this.move = move;
	}
	
	@Override
	public void executeMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());	
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
		move.executeMove(board);
	}

	@Override
	public void executeMove(BoardState boardState) {
		move.executeMove(boardState);
	}

	@Override
	public void undoMove(BoardState boardState) {
		move.executeMove(boardState);
	}

	@Override
	public void executeMove(ColorBoard coloBoard) {
		move.executeMove(coloBoard);
	}

	@Override
	public void undoMove(ColorBoard colorBoard) {
		move.executeMove(colorBoard);
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		move.executeMove(moveCache);
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		move.executeMove(moveCache);
	}
	
	@Override
	public int compareTo(Move theOther) {
		//Comparamos from
		if(this.getFrom().getKey().getRank() > theOther.getFrom().getKey().getRank()){
			return 1;
		} else if (this.getFrom().getKey().getRank() < theOther.getFrom().getKey().getRank()){
			return -1;
		}
		

		if(this.getFrom().getKey().getFile() <  theOther.getFrom().getKey().getFile()){
			return 1;
		} else if(this.getFrom().getKey().getFile() >  theOther.getFrom().getKey().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.getTo().getKey().getRank() < theOther.getTo().getKey().getRank()){
			return 1;
		} else if (this.getTo().getKey().getRank() > theOther.getTo().getKey().getRank()){
			return -1;
		}
		

		if(this.getTo().getKey().getFile() <  theOther.getTo().getKey().getFile()){
			return -1;
		} else if(this.getTo().getKey().getFile() >  theOther.getTo().getKey().getFile()){
			return 1;
		}
		
		//--------------- Desde y hasta coinciden, que hacemos ?
		
		return 0;
	}
	
	@Override
	public String toString() {
		return move.getFrom().toString() + " " + move.getTo().toString() + " - " + getType();
	}	
}
