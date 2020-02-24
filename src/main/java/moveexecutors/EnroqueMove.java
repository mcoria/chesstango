package moveexecutors;

import java.util.List;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Square;

public abstract class EnroqueMove implements Comparable<Move>, Move {
	
	protected abstract Move getReyMove();
	protected abstract Move getTorreMove();
	
	@Override
	public void executeMove(DummyBoard board) {
		getReyMove().executeMove(board);
		getTorreMove().executeMove(board);
	}


	@Override
	public void undoMove(DummyBoard board) {
		getReyMove().undoMove(board);
		getTorreMove().undoMove(board);
	}	
	
	@Override
	public void undoState(BoardState boardState) {
		boardState.popState();		
	}	
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		getReyMove().executeSquareLists(squaresTurno, squaresOpenente);
		getTorreMove().executeSquareLists(squaresTurno, squaresOpenente);
	}

	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		getReyMove().undoSquareLists(squaresTurno, squaresOpenente);
		getTorreMove().undoSquareLists(squaresTurno, squaresOpenente);
	}
	
	@Override
	public int hashCode() {
		return getFrom().getKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof EnroqueMove){
			EnroqueMove theOther = (EnroqueMove) obj;
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
		
		return 0;
	}

	@Override
	public String toString() {
		return getFrom().toString() + " " + getTo().toString() + "; " + getType();
	}

	protected abstract String getType();
}
