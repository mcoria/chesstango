package chess;

import java.util.Objects;

import moveexecutors.MoveExecutor;

public class Move implements Comparable<Move>{
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
		return (this.from.getRank() - theOther.from.getRank()) * 8 + (this.from.getFile() - theOther.from.getFile()) * 64 +
				   (this.to.getRank() - theOther.to.getRank()) * 8 + (this.to.getFile() - theOther.to.getFile());
	}

	public void execute(BoardMediator board) {
		moveExecutor.execute(board, this);
	}

	public void undo(BoardMediator board) {
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
