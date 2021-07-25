package moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.Move;
import chess.PosicionPieza;
import layers.MoveCacheBoard;
import movecalculators.MoveFilter;

public abstract class AbstractMove implements Move {
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
	public void executeMove(Board board) {
		board.executeMove(this);
	}
	
	@Override
	public void undoMove(Board board) {
		board.undoMove(this);
	}

	@Override
	public void executeMove(BoardState boardState) {
		boardState.pushState();
		boardState.rollTurno();
		boardState.setPeonPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePeonMove
	}
	
	@Override
	public void undoMove(BoardState boardState) {
		boardState.popState();		
	}

	@Override
	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey());
	}

	@Override
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.popState();
	}
	
	@Override
	public boolean filter(MoveFilter filter){
		return filter.filterMove(this);
	}	
	
	@Override
	public int hashCode() {
		return from.getKey().hashCode();
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
		return from.toString() + " " + to.toString() + " - " + this.getClass().getSimpleName().toString();
	}
	


}
