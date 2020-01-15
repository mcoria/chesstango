package chess;

import java.util.Objects;

import moveexecutors.MoveExecutor;

public class Move implements Comparable<Move> {
	private Square from;
	private Square to;
	
	private MoveExecutor moveExecutor;	
	
	public Move(Square from, Square to, MoveExecutor moveExecutor) {
		this.from = from;
		this.to = to;
		this.moveExecutor = moveExecutor;
	}	

	public Square getFrom() {
		return from;
	}

	public Square getTo() {
		return to;
	}
	
	@Override
	public int hashCode() {
		return from.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return from.equals(theOther.from) &&  to.equals(theOther.to) && Objects.equals(moveExecutor, theOther.moveExecutor);
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		//Comparamos from
		if(this.from.getRank() > theOther.from.getRank()){
			return 1;
		} else if (this.from.getRank() < theOther.from.getRank()){
			return -1;
		}
		

		if(this.from.getFile() <  theOther.from.getFile()){
			return 1;
		} else if(this.from.getFile() >  theOther.from.getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getRank() < theOther.to.getRank()){
			return 1;
		} else if (this.to.getRank() > theOther.to.getRank()){
			return -1;
		}
		

		if(this.to.getFile() <  theOther.to.getFile()){
			return -1;
		} else if(this.to.getFile() >  theOther.to.getFile()){
			return 1;
		}		
		
		return 0;
	}

	public void execute(DummyBoard board) {
		moveExecutor.execute(board, this);
	}

	public void undo(DummyBoard board) {
		moveExecutor.undo(board, this);
	}

	public MoveExecutor getMoveExecutor() {
		return moveExecutor;
	}

	public void setMoveExecutor(MoveExecutor moveExecutor) {
		this.moveExecutor = moveExecutor;
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + "; " + (moveExecutor == null ? "ERROR" : moveExecutor.toString());
	}

}
