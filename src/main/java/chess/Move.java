package chess;


public class Move {
	private Square from;
	private Square to;
	
	public Move(Square from, Square to) {
		this.from = from;
		this.to = to;
	}

	public Square getFrom() {
		return from;
	}

	public Square getTo() {
		return to;
	}
}
