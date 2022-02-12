package chess.position;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.Color;
import chess.Piece;
import chess.builder.imp.PiecePlacementBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.debug.chess.ColorBoardDebug;
import chess.fen.FENDecoder;
import chess.iterators.square.SquareIterator;


/**
 * @author Mauricio Coria
 *
 */
public class ColorBoardTest {

	private ColorBoard colorBoard;

	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		PiecePlacement tablero = getTablero("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.WHITE); iterator.hasNext();) {
			Piece piece = tablero.getPieza(iterator.next());
			assertEquals(Color.WHITE, piece.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}
	
	
	private PiecePlacement getTablero(String string) {		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new DebugChessFactory());

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	

}
