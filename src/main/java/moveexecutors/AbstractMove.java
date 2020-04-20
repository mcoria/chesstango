package moveexecutors;

import java.util.List;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public abstract class AbstractMove implements Comparable<Move>, Move {
	protected PosicionPieza from;
	protected PosicionPieza to;
	
	
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
	public void undoMove(DummyBoard board) {
		board.setPosicion(to);							//Reestablecemos destino
		board.setPosicion(from);						//Volvemos a origen
	}

	@Override
	public void executeState(BoardState boardState) {
		boardState.pushState();
		boardState.rollTurno();
		boardState.setPeonPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePeonMove
	}
	
	@Override
	public void undoState(BoardState boardState) {
		boardState.popState();		
	}	
	
	@Override
	public void executeSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(from.getKey());
		squaresTurno.add(to.getKey());
	}
	
	@Override
	public void undoSquareLists(List<Square> squaresTurno, List<Square> squaresOpenente) {
		squaresTurno.remove(to.getKey());
		squaresTurno.add(from.getKey());
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
		return from.toString() + " " + to.toString() + " - " + getType();
	}

	protected abstract String getType();


}
