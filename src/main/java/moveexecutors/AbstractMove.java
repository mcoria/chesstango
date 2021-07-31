package moveexecutors;

import chess.BoardState;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;
import layers.MoveCacheBoard;

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

	//TODO: implementar un decorator antes de crear el movimiento
	@Override
	public void executeMove(BoardState boardState) {
		boardState.pushState();
		boardState.rollTurno();
		boardState.setPeonPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePeonMove
		
		if(from.getKey().equals(Square.a1) || to.getKey().equals(Square.a1)){
			boardState.setEnroqueBlancoReinaPermitido(false);
		}
		
		if(from.getKey().equals(Square.h1) || to.getKey().equals(Square.h1)){
			boardState.setEnroqueBlancoReyPermitido(false);
		}
		
		if(from.getKey().equals(Square.a8) || to.getKey().equals(Square.a8)){
			boardState.setEnroqueNegroReinaPermitido(false);
		}
		
		if(from.getKey().equals(Square.h8) || to.getKey().equals(Square.h8)){
			boardState.setEnroqueNegroReyPermitido(false);
		}		
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
