package moveexecutors;

import java.util.Map;

import chess.BoardState;
import chess.Move;
import chess.Pieza;
import chess.Square;

public abstract class AbstractMove implements Comparable<AbstractMove>, Move {
	protected Map.Entry<Square, Pieza> from;
	protected Map.Entry<Square, Pieza> to;
	
	
	public AbstractMove(Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
		this.from = from;
		this.to = to;
	}	

	@Override
	public Map.Entry<Square, Pieza> getFrom() {
		return from;
	}

	@Override
	public Map.Entry<Square, Pieza> getTo() {
		return to;
	}
	
	@Override
	public void undoState(BoardState boardState) {
		boardState.popState();		
	}	
	
	@Override
	public int hashCode() {
		return from.getKey().hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof AbstractMove){
			AbstractMove theOther = (AbstractMove) obj;
			return from.equals(theOther.from) &&  to.equals(theOther.to);
		}
		return false;
	}

	@Override
	public int compareTo(AbstractMove theOther) {
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

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + "; " + getType();
	}

	protected abstract String getType();


}
