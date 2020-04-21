package iterators;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.Square;

public class CardinalSquareIteratorTest01 {

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
	
	@Test
	public void testNorteEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.NorteEste, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f6, iterator.next());
		assertEquals(Square.g7, iterator.next());
		assertEquals(Square.h8, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testSurEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.SurEste, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f4, iterator.next());
		assertEquals(Square.g3, iterator.next());
		assertEquals(Square.h2, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testSurOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.SurOeste, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d4, iterator.next());
		assertEquals(Square.c3, iterator.next());
		assertEquals(Square.b2, iterator.next());
		assertEquals(Square.a1, iterator.next());		
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testNorteOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Cardinal.NorteOeste, Square.e5);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d6, iterator.next());
		assertEquals(Square.c7, iterator.next());
		assertEquals(Square.b8, iterator.next());		
		assertFalse(iterator.hasNext());
	}	
}
