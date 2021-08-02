package chess;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import builder.ChessBuilderParts;
import debug.builder.DebugChessFactory;
import iterators.SquareIterator;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import parsers.FENParser;

public class ColorBoardTest {

	private ColorBoard colorBoard;

	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		PosicionPiezaBoard tablero = getTablero("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		colorBoard = new ColorBoard(tablero);
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.BLANCO); iterator.hasNext();) {
			Pieza pieza = tablero.getPieza(iterator.next());
			assertEquals(Color.BLANCO, pieza.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}
	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());

		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}	

}
