package moveexecutors;

import chess.Move;
import chess.MoveCache;
import chess.PosicionPieza;
import chess.Square;

public abstract class AbstractMove implements Comparable<Move>, Move { //, Cloneable
	protected final PosicionPieza from;
	protected final PosicionPieza to;
	
	public AbstractMove(PosicionPieza from, PosicionPieza to) {
		this.from = from;
		this.to = to;
	}	

	@Override
	public PosicionPieza getFrom() {
		return from;
	}

	@Override
	public PosicionPieza getTo() {
		return to;
	}	
	
	@Override
	public int hashCode() {
		return from.getKey().hashCode();
	}
	
	@Override
	public void executeMove(MoveCache moveCache) {
		updateMoveChache(moveCache);
	}

	@Override
	public void undoMove(MoveCache moveCache) {
		updateMoveChache(moveCache);
	}
	
	public void updateMoveChache(MoveCache moveCache) {
		Square[] squares = Square.values();
		for (Square square : squares) {
			moveCache.emptyContainversAffectedBy(square);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Move){
			Move theOther = (Move) obj;
			return from.equals(theOther.getFrom()) &&  to.equals(theOther.getTo());
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		//Comparamos from
		if(this.from.getKey().getRank() > theOther.getFrom().getKey().getRank()){
			return 1;
		} else if (this.from.getKey().getRank() < theOther.getFrom().getKey().getRank()){
			return -1;
		}
		

		if(this.from.getKey().getFile() <  theOther.getFrom().getKey().getFile()){
			return 1;
		} else if(this.from.getKey().getFile() >  theOther.getFrom().getKey().getFile()){
			return -1;
		}
		
		//---------------
		//Son iguales asi que comparamos to
		if(this.to.getKey().getRank() < theOther.getTo().getKey().getRank()){
			return 1;
		} else if (this.to.getKey().getRank() > theOther.getTo().getKey().getRank()){
			return -1;
		}
		

		if(this.to.getKey().getFile() <  theOther.getTo().getKey().getFile()){
			return -1;
		} else if(this.to.getKey().getFile() >  theOther.getTo().getKey().getFile()){
			return 1;
		}
		
		//--------------- Desde y hasta coinciden, que hacemos ?
		
		
		return 0;
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + " - " + getType();
	}
	
	/*
	@Override
	public Move clone() {
		try {
			return (Move) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}*/
	
	protected abstract String getType();


}
