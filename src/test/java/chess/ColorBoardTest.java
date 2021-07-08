package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import parsers.FENBoarBuilder;

public class ColorBoardTest {

	private FENBoarBuilder<ChessBuilder> builder;
	
	private ColorBoard colorBoard;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
	}
	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		PosicionPiezaBoard tablero = builder.constructDefaultBoard().getBuilder().getPosicionPiezaBoard();
		
		colorBoard = new ColorBoard(tablero);
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.BLANCO); iterator.hasNext();) {
			Pieza pieza = tablero.getPieza(iterator.next());
			assertEquals(Color.BLANCO, pieza.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}

}
