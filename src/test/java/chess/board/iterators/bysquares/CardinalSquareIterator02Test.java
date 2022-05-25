package chess.board.iterators.bysquares;

import static org.junit.Assert.*;

import org.junit.Test;

import chess.board.Square;
import chess.board.iterators.Cardinal;


/**
 * @author Mauricio Coria
 *
 */
public class CardinalSquareIterator02Test {

	@Test
	public void testNorte() {
		assertTrue(Cardinal.Norte.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.d5));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.d5));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.d5));
	}

	@Test
	public void testSur() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.d1));
		assertTrue(Cardinal.Sur.isInDirection(Square.d3, Square.d1));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.d1));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.d1));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.d1));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.d1));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.d1));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.d1));
	}
	
	@Test
	public void testEste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.f3));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.f3));
		assertTrue(Cardinal.Este.isInDirection(Square.d3, Square.f3));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.f3));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.f3));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.f3));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.f3));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.f3));
	}
	
	@Test
	public void testOeste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.a3));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.a3));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.a3));
		assertTrue(Cardinal.Oeste.isInDirection(Square.d3, Square.a3));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.a3));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.a3));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.a3));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.a3));
	}
	
	@Test
	public void testNorteEste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.f5));
		
		assertTrue(Cardinal.NorteEste.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.f5));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.f5));
	}
	
	@Test
	public void testSurEste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.f1));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.f1));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.f1));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.f1));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.f1));
		assertTrue(Cardinal.SurEste.isInDirection(Square.d3, Square.f1));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.f1));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.f1));
	}
	
	@Test
	public void testSurOeste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.b1));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.b1));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.b1));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.b1));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.b1));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.b1));
		assertTrue(Cardinal.SurOeste.isInDirection(Square.d3, Square.b1));
		assertFalse(Cardinal.NorteOeste.isInDirection(Square.d3, Square.b1));
	}
	
	@Test
	public void testNorteOeste() {
		assertFalse(Cardinal.Norte.isInDirection(Square.d3, Square.b5));
		assertFalse(Cardinal.Sur.isInDirection(Square.d3, Square.b5));
		assertFalse(Cardinal.Este.isInDirection(Square.d3, Square.b5));
		assertFalse(Cardinal.Oeste.isInDirection(Square.d3, Square.b5));
		
		assertFalse(Cardinal.NorteEste.isInDirection(Square.d3, Square.b5));
		assertFalse(Cardinal.SurEste.isInDirection(Square.d3, Square.b5));
		assertFalse(Cardinal.SurOeste.isInDirection(Square.d3, Square.b5));
		assertTrue(Cardinal.NorteOeste.isInDirection(Square.d3, Square.b5));
	}	
}
