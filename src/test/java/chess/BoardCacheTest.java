package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import iterators.SquareIterator;
import parsers.FENBoarBuilder;

public class BoardCacheTest {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		DummyBoard tablero = builder.withDefaultBoard().buildDummyBoard();
		
		BoardCache boardCache = new BoardCache(tablero);
		
		for (SquareIterator iterator = boardCache.iteratorSquare(Color.BLANCO); iterator.hasNext();) {
			Pieza pieza = tablero.getPieza(iterator.next());
			assertEquals(Color.BLANCO, pieza.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}

}
