package iterators;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.Square;
import iterators.CardinalSquareIterator.Cardinal;

public class CardinalSquareIteratorTest {

	@Test
	public void test() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.Norte, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.e6, iterator.next());
		assertEquals(Square.e7, iterator.next());
		assertEquals(Square.e8, iterator.next());
	}

}
