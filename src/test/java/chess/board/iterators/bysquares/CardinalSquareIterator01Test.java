package chess.board.iterators.bysquares;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.board.Square;
import chess.board.iterators.Cardinal;


/**
 * @author Mauricio Coria
 *
 */
public class CardinalSquareIterator01Test {

	@Test
	public void testNorte() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.Norte);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.e6, iterator.next());
		assertEquals(Square.e7, iterator.next());
		assertEquals(Square.e8, iterator.next());
		assertFalse(iterator.hasNext());
	}

	@Test
	public void testSur() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.Sur);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.e4, iterator.next());
		assertEquals(Square.e3, iterator.next());
		assertEquals(Square.e2, iterator.next());
		assertEquals(Square.e1, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.Este);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f5, iterator.next());
		assertEquals(Square.g5, iterator.next());
		assertEquals(Square.h5, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.Oeste);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d5, iterator.next());
		assertEquals(Square.c5, iterator.next());
		assertEquals(Square.b5, iterator.next());
		assertEquals(Square.a5, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testNorteEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.NorteEste);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f6, iterator.next());
		assertEquals(Square.g7, iterator.next());
		assertEquals(Square.h8, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testSurEste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.SurEste);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.f4, iterator.next());
		assertEquals(Square.g3, iterator.next());
		assertEquals(Square.h2, iterator.next());
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testSurOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.SurOeste);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d4, iterator.next());
		assertEquals(Square.c3, iterator.next());
		assertEquals(Square.b2, iterator.next());
		assertEquals(Square.a1, iterator.next());		
		assertFalse(iterator.hasNext());
	}
	
	@Test
	public void testNorteOeste() {
		CardinalSquareIterator iterator = new CardinalSquareIterator(Square.e5, Cardinal.NorteOeste);
		
		assertTrue(iterator.hasNext());
		assertEquals(Square.d6, iterator.next());
		assertEquals(Square.c7, iterator.next());
		assertEquals(Square.b8, iterator.next());		
		assertFalse(iterator.hasNext());
	}	
}
