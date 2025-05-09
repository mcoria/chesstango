package net.chesstango.board.iterators.bysquare;

import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;

/**
 * @author Mauricio Coria
 *
 */
public class CardinalSquareIterator implements SquareIterator {
	
	private final Cardinal cardinal;
	private Square nextPoint;
	
	public CardinalSquareIterator(Square startingPoint, Cardinal cardinal) {
		this.cardinal = cardinal;
		this.nextPoint = this.cardinal.nextSquare(startingPoint);
	}

	@Override
	public boolean hasNext() {
		return nextPoint !=null;
	}

	@Override
	public Square next() {
		Square currentPoint = nextPoint;
		this.nextPoint = this.cardinal.nextSquare(currentPoint);
		return currentPoint;
	}

}
