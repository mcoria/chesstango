package iterators;

import chess.Square;

public class CardinalSquareIterator implements SquareIterator {
	
	private final Cardinal cardinal;
	private Square nextPoint;
	
	public CardinalSquareIterator(Cardinal cardinal, Square startingPoint) {
		this.cardinal = cardinal;
		this.nextPoint = this.cardinal.calcularNextPoint(startingPoint);
	}

	@Override
	public boolean hasNext() {
		return nextPoint !=null;
	}

	@Override
	public Square next() {
		Square currentPoint = nextPoint;
		this.nextPoint = this.cardinal.calcularNextPoint(currentPoint);
		return currentPoint;
	}

}
