package chess;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import moveexecutors.SimpleMove;

public class MoveCacheTest {

	private MoveCache cache;
	
	@Before
	public void setUp() throws Exception {
		cache = new MoveCache();
	}
	
	@Test
	public void test01() {
		
		Collection<Move> container1 = new ArrayList<Move>();
		container1.add(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a3, null)));
		container1.add(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, container1);
		cache.setAffectedBy(Square.a2,  Arrays.asList(new Square[] {Square.a1, Square.b1}));
		
		
		Collection<Move> container2 = new ArrayList<Move>();
		container2.add(new SimpleMove(new PosicionPieza(Square.b2, Pieza.PEON_BLANCO), new PosicionPieza(Square.b3, null)));
		container2.add(new SimpleMove(new PosicionPieza(Square.b2, Pieza.PEON_BLANCO), new PosicionPieza(Square.b4, null)));
		cache.setPseudoMoves(Square.b2, container2);
		cache.setAffectedBy(Square.b2,  Arrays.asList(new Square[] {Square.a1, Square.b1}));
		
		
		assertEquals(container1, cache.getPseudoMoves(Square.a2));
		assertEquals(container2, cache.getPseudoMoves(Square.b2));
		
		cache.clearPseudoMovesAffectedBy(Square.a1);
		
		assertNull(cache.getPseudoMoves(Square.a2));
		assertNull(cache.getPseudoMoves(Square.b2));
		
	}
	
	@Test
	public void test02() {
		
		Collection<Move> container1 = new ArrayList<Move>();
		container1.add(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a3, null)));
		container1.add(new SimpleMove(new PosicionPieza(Square.a2, Pieza.PEON_BLANCO), new PosicionPieza(Square.a4, null)));
		cache.setPseudoMoves(Square.a2, container1);
		cache.setAffectedBy(Square.a2,  Arrays.asList(new Square[] {Square.a1, Square.b1}));
		
		
		Collection<Move> container2 = new ArrayList<Move>();
		container2.add(new SimpleMove(new PosicionPieza(Square.b2, Pieza.PEON_BLANCO), new PosicionPieza(Square.b3, null)));
		container2.add(new SimpleMove(new PosicionPieza(Square.b2, Pieza.PEON_BLANCO), new PosicionPieza(Square.b4, null)));
		cache.setPseudoMoves(Square.b2, container2);
		cache.setAffectedBy(Square.b2,  Arrays.asList(new Square[] {Square.a1, Square.b1}));
		
		
		assertEquals(container1, cache.getPseudoMoves(Square.a2));
		assertEquals(container2, cache.getPseudoMoves(Square.b2));
		
		cache.clearPseudoMovesAffectedBy(Square.b1);
		
		assertNull(cache.getPseudoMoves(Square.a2));
		assertNull(cache.getPseudoMoves(Square.b2));
		
	}	

}
