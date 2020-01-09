package chess;


public class Move implements Comparable<Move>{
	private Square from;
	private Square to;
	private MoveType type;
	
	public enum MoveType {
		SIMPLE,
		CAPTURA,
		ENROQUE
	}
	
	public Move(Square from, Square to, MoveType type) {
		this.from = from;
		this.to = to;
		this.type = type;
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
			return from.equals(theOther.from) &&  to.equals(theOther.to) && type.equals(theOther.type);
		}
		return false;
	}

	@Override
	public int compareTo(Move theOther) {
		return (this.from.getRank() - theOther.from.getRank()) * 8 + (this.from.getFile() - theOther.from.getFile()) * 64 +
				   (this.to.getRank() - theOther.to.getRank()) * 8 + (this.to.getFile() - theOther.to.getFile());
	}

	public void execute(Board board) {
		
	}

	public void execute(DummyBoard tablero) {
		// TODO Auto-generated method stub
		
	}
}
