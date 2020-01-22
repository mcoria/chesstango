package iterators;

import java.util.AbstractMap.SimpleImmutableEntry;

import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public class DummyBoardIterator implements BoardIterator {
	
	private SquareIterator squareIterator;
	private DummyBoard board;
	
	public DummyBoardIterator(DummyBoard board, SquareIterator squareIterator) {
		this.squareIterator = squareIterator;
		this.board = board;
	}

	@Override
	public boolean hasNext() {
		return squareIterator.hasNext();
	}
	
	@Override
	public SimpleImmutableEntry<Square, Pieza> next() {
		Square currentSquare = squareIterator.next();
		Pieza pieza = board.getPieza(currentSquare);
		return new SimpleImmutableEntry<Square, Pieza>(currentSquare, pieza);
	}

}
