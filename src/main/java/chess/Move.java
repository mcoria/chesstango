package chess;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import moveexecutors.CaptureMoveExecutor;
import moveexecutors.CapturePeonPasanteExecutor;
import moveexecutors.EnroqueBlancoReyMoveExecutor;
import moveexecutors.EnroqueBlancoReynaMoveExecutor;
import moveexecutors.EnroqueNegroReyMoveExecutor;
import moveexecutors.EnroqueNegroReynaMoveExecutor;
import moveexecutors.MoveExecutor;
import moveexecutors.SaltoDoblePeonMoveExecutor;
import moveexecutors.SimpleMoveExecutor;

public class Move implements Comparable<Move> {
	
	public static final Move MOVE_ENROQUE_BLANCO_REINA = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO), new SimpleImmutableEntry<Square, Pieza>(Square.c1, null), MoveType.ENROQUE_BLANCO_REINA);
	public static final Move MOVE_ENROQUE_BLANCO_REY = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e1, Pieza.REY_BLANCO), new SimpleImmutableEntry<Square, Pieza>(Square.g1, null), MoveType.ENROQUE_BLANCO_REY);
	
	public static final Move MOVE_ENROQUE_NEGRO_REYNA = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO), new SimpleImmutableEntry<Square, Pieza>(Square.c8, null), MoveType.ENROQUE_NEGRO_REINA);
	public static final Move MOVE_ENROQUE_NEGRO_REY = new Move(new SimpleImmutableEntry<Square, Pieza>(Square.e8, Pieza.REY_NEGRO), new SimpleImmutableEntry<Square, Pieza>(Square.g8, null), MoveType.ENROQUE_NEGRO_REY);	
	
	private Map.Entry<Square, Pieza> from;
	private Map.Entry<Square, Pieza> to;
	private MoveType moveType;
	
	
	public enum MoveType implements MoveExecutor {
		SIMPLE(new SimpleMoveExecutor()),
		SALTO_DOBLE_PEON(new SaltoDoblePeonMoveExecutor()),
		CAPTURA(new CaptureMoveExecutor()),
		CAPTURA_PEON_PASANTE(new CapturePeonPasanteExecutor()),
		ENROQUE_BLANCO_REINA(new EnroqueBlancoReynaMoveExecutor()),
		ENROQUE_BLANCO_REY(new EnroqueBlancoReyMoveExecutor()),				
		ENROQUE_NEGRO_REINA(new EnroqueNegroReynaMoveExecutor()),
		ENROQUE_NEGRO_REY(new EnroqueNegroReyMoveExecutor());
		
		private MoveExecutor executor = null;
		private MoveType(MoveExecutor executor) {
			this.executor = executor;
		}
		
		@Override
		public void execute(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to) {
			executor.execute(board, boardState, from, to);
		}
		
		@Override
		public void undo(DummyBoard board, BoardState boardState) {
			executor.undo(board, boardState);
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
		return from.getKey().hashCode();
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
		moveType.execute(board, boardState, from, to);
		boardState.rollTurno();
	}

	public void undo(DummyBoard board, BoardState boardState) {
		boardState.rollTurno();
		moveType.undo(board, boardState);
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
