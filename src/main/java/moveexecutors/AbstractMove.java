package moveexecutors;

import chess.Board;
import chess.BoardState;
import chess.Move;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import layers.MoveCacheBoard;

public abstract class AbstractMove implements Comparable<Move>, Move {
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
	
	///////////////////////////// START Move execution Logic /////////////////////////////
	
	public abstract void executeMove(ColorBoard colorBoard);
	public abstract void undoMove(ColorBoard colorBoard);
	
	public abstract void undoMove(PosicionPiezaBoard dummyBoard);
	public abstract void executeMove(PosicionPiezaBoard dummyBoard);
	
	@Override
	public void executePseudo(Board board){
		executeMove(board.getDummyBoard());
		executeMove(board.getColorBoard());	
	}
	
	@Override
	public void undoPseudo(Board board){
		undoMove(board.getColorBoard());
		undoMove(board.getDummyBoard());	
	}
	
	@Override
	public void execute(Board board) {
		//colorBoard.validar(dummyBoard);
		//moveCache.validar();

		executeMove(board.getDummyBoard());

		executeMove(board.getColorBoard());

		executeMove(board.getMoveCache());

		executeMove(board.getBoardState());

		//moveCache.validar();
		//colorBoard.validar(dummyBoard);
	}

	@Override
	public void undo(Board board) {
		//colorBoard.validar(dummyBoard);
		//moveCache.validar();

		undoMove(board.getBoardState());

		undoMove(board.getMoveCache());

		undoMove(board.getColorBoard());

		undoMove(board.getDummyBoard());

		//moveCache.validar();
		//colorBoard.validar(dummyBoard);
	}
	///////////////////////////// END Move execution Logic /////////////////////////////

	public void executeMove(BoardState boardState) {
		boardState.pushState();
		boardState.rollTurno();
		boardState.setPeonPasanteSquare(null); 			// Por defecto en null y solo escribimos en SaltoDoblePeonMove
	}
	

	public void undoMove(BoardState boardState) {
		boardState.popState();		
	}


	public void executeMove(MoveCacheBoard moveCache) {
		moveCache.pushState();
		moveCache.clearPseudoMoves(from.getKey(), to.getKey());
	}

	
	public void undoMove(MoveCacheBoard moveCache) {
		moveCache.popState();
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
