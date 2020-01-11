package chess;

import java.util.Objects;

import moveexecutors.MoveExecutor;
import moveexecutors.SimpleMoveExecutor;

public class Move implements Comparable<Move>{
	private Square from;
	private Square to;
	private MoveType type;
	private Pieza capturada;
	
	public enum MoveType {
		SIMPLE(new SimpleMoveExecutor()),
		CAPTURA(new SimpleMoveExecutor()),
		ENROQUE(null);
		
		private MoveExecutor executor = null;
		private MoveType(MoveExecutor executor) {
			this.executor = executor;
		}
		
		public MoveExecutor getMoveExecutor() {
			return executor;
		}
	}
	
	public Move(Square from, Square to) {
		this.from = from;
		this.to = to;
		this.type = MoveType.SIMPLE;
	}
	
	public Move(Square from, Square to, Pieza capturada) {
		this.from = from;
		this.to = to;
		this.type = MoveType.CAPTURA;
		this.capturada = capturada;
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
			return from.equals(theOther.from) &&  to.equals(theOther.to) && type.equals(theOther.type) && Objects.equals(capturada, theOther.capturada);
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		return (this.from.getRank() - theOther.from.getRank()) * 8 + (this.from.getFile() - theOther.from.getFile()) * 64 +
				   (this.to.getRank() - theOther.to.getRank()) * 8 + (this.to.getFile() - theOther.to.getFile());
	}

	public void execute(BoardMediator board) {
		this.type.getMoveExecutor().execute(board, this);
	}

	public void undo(BoardMediator board) {
		this.type.getMoveExecutor().undo(board, this);
	}
	
	public void setCapturada(Pieza pieza) {
		this.capturada = pieza;
		
	}

	public Pieza getCapturada() {
		return capturada;
	}

}
