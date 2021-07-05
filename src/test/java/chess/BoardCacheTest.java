package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import parsers.FENBoarBuilder;

public class BoardCacheTest {

	private FENBoarBuilder<ChessBuilder> builder;
	
	private ChessBuilder chessBuilder; 

	@Before
	public void setUp() throws Exception {
		chessBuilder = new ChessBuilder();
		builder = new FENBoarBuilder<ChessBuilder>(chessBuilder);
	}
	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		PosicionPiezaBoard tablero = builder.constructDefaultBoard().getBuilder().buildPosicionPiezaBoard();
		ColorBoard colorBoard = chessBuilder.buildColorBoard();
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.BLANCO); iterator.hasNext();) {
			Pieza pieza = tablero.getPieza(iterator.next());
			assertEquals(Color.BLANCO, pieza.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}

}
