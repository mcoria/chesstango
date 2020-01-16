package chess;

import java.util.Map;
import java.util.Objects;

import moveexecutors.MoveExecutor;

public class Move implements Comparable<Move> {
	private Map.Entry<Square, Pieza> from;
	private Map.Entry<Square, Pieza> to;
	
	private MoveExecutor moveExecutor;	
	
	public Move(Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to, MoveExecutor moveExecutor) {
		this.from = from;
		this.to = to;
		this.moveExecutor = moveExecutor;
	}	

	public Map.Entry<Square, Pieza> getFrom() {
		return from;
	}

	public Map.Entry<Square, Pieza> getTo() {
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
		if(this.from.getKey().getRank() > theOther.from.getKey().getRank()){
			return 1;
		} else if (this.from.getKey().getRank() < theOther.from.getKey().getRank()){
			return -1;
		}
		

		if(this.from.getKey().getFile() <  theOther.from.getKey().getFile()){
			return 1;
		} else if(this.from.getKey().getFile() >  theOther.from.getKey().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getKey().getRank() < theOther.to.getKey().getRank()){
			return 1;
		} else if (this.to.getKey().getRank() > theOther.to.getKey().getRank()){
			return -1;
		}
		

		if(this.to.getKey().getFile() <  theOther.to.getKey().getFile()){
			return -1;
		} else if(this.to.getKey().getFile() >  theOther.to.getKey().getFile()){
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
