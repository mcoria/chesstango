package chess;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.iterators.SquareIterator;
import chess.layers.ColorBoard;
import chess.layers.PosicionPiezaBoard;
import chess.parsers.FENParser;


/**
 * @author Mauricio Coria
 *
 */
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
