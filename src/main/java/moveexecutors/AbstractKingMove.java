package moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.KingMove;
import chess.Move;
import chess.Pieza;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.KingCacheBoard;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;
import movecalculators.MoveFilter;

public abstract class AbstractKingMove implements KingMove {
	
	protected Move move;
	
	public AbstractKingMove(Move move) {
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
	public void executeMove(Board board) {
		board.executeMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
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
		if(Pieza.REY_BLANCO.equals(move.getFrom().getValue()) ){
			boardState.setEnroqueBlancoReinaPermitido(false);
			boardState.setEnroqueBlancoReyPermitido(false);
		} else {
			boardState.setEnroqueNegroReinaPermitido(false);
			boardState.setEnroqueNegroReyPermitido(false);
		}
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
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getTo().getKey());
	}
	
	@Override
	public void undoMove(KingCacheBoard kingCacheBoard){
		kingCacheBoard.setKingSquare(getFrom().getValue().getColor(), getFrom().getKey());	
	}
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return getFrom().equals(theOther.getFrom()) &&  getTo().equals(theOther.getTo());
		}
		return false;
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
		return move.getFrom().toString() + " " + move.getTo().toString() + " - " + this.getClass().getSimpleName().toString();
	}	
}
