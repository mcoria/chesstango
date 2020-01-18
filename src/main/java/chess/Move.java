package chess;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import moveexecutors.CaptureMoveExecutor;
import moveexecutors.CapturePeonPasanteExecutor;
import moveexecutors.EnroqueMoveExecutor;
import moveexecutors.MoveExecutor;
import moveexecutors.SaltoDoblePeonMoveExecutor;
import moveexecutors.SimpleMoveExecutor;

public class Move implements Comparable<Move> {
	
	public static final Move ENROQUE_BLANCO_REYNA = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO), new SimpleImmutableEntry<Square, Pieza>(Square.c1, null), MoveType.ENROQUE);
	public static final Move ENROQUE_BLANCO_REY = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO), new SimpleImmutableEntry<Square, Pieza>(Square.g1, null), MoveType.ENROQUE);
	
	private Map.Entry<Square, Pieza> from;
	private Map.Entry<Square, Pieza> to;
	private MoveType moveType;
	
	
	public enum MoveType implements MoveExecutor{
		SIMPLE(new SimpleMoveExecutor()),
		SALTO_DOBLE_PEON(new SaltoDoblePeonMoveExecutor()),
		CAPTURA(new CaptureMoveExecutor()),
		CAPTURA_PEON_PASANTE(new CapturePeonPasanteExecutor()),
		ENROQUE(new EnroqueMoveExecutor());
		
		private MoveExecutor executor = null;
		private MoveType(MoveExecutor executor) {
			this.executor = executor;
		}
		
		@Override
		public void execute(DummyBoard board, BoardState boardState, Move move) {
			executor.execute(board, boardState, move);
		}
		
		@Override
		public void undo(DummyBoard board, BoardState boardState, Move move) {
			executor.undo(board, boardState, move);
		}
	}
	
	public Move(Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to, MoveType moveType) {
		this.from = from;
		this.to = to;
		this.moveType = moveType;
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
			return from.equals(theOther.from) &&  to.equals(theOther.to) && this.moveType.equals(theOther.moveType);
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

	public void execute(DummyBoard board, BoardState boardState) {
		boardState.pushState();
		this.moveType.execute(board, boardState, this);
	}

	public void undo(DummyBoard board, BoardState boardState) {
		this.moveType.undo(board, boardState, this);
		boardState.popState();
	}

	@Override
	public String toString() {
		return from.toString() + " " + to.toString() + "; " + (moveType == null ? "ERROR" : moveType.toString());
	}

	public MoveType getMoveType() {
		return moveType;
	}

	public void setMoveType(MoveType moveType) {
		this.moveType = moveType;
	}

}
