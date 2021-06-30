package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import iterators.SquareIterator;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
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
		
		PosicionPiezaBoard tablero = builder.withDefaultBoard().buildPosicionPiezaBoard();
		
		ColorBoard colorBoard = builder.buildColorBoard();
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.BLANCO); iterator.hasNext();) {
			Pieza pieza = tablero.getPieza(iterator.next());
			assertEquals(Color.BLANCO, pieza.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}

}
