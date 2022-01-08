package chess.iterators;

import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class CardinalSquareIterator implements SquareIterator {
	
	private final Cardinal cardinal;
	private Square nextPoint;
	
	public CardinalSquareIterator(Square startingPoint, Cardinal cardinal) {
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
