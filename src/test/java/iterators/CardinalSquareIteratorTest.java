package iterators;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.Square;
import iterators.CardinalSquareIterator.Cardinal;

public class CardinalSquareIteratorTest {

	@Test
	public void testNorte() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.Norte, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.e6, iterator.next());
		assertEquals(Square.e7, iterator.next());
		assertEquals(Square.e8, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testSur() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.Sur, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.e4, iterator.next());
		assertEquals(Square.e3, iterator.next());
		assertEquals(Square.e2, iterator.next());
		assertEquals(Square.e1, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.Este, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f5, iterator.next());
		assertEquals(Square.g5, iterator.next());
		assertEquals(Square.h5, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.Oeste, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d5, iterator.next());
		assertEquals(Square.c5, iterator.next());
		assertEquals(Square.b5, iterator.next());
		assertEquals(Square.a5, iterator.next());
		assertFalse(iterator.hasNext());
	}	
}
