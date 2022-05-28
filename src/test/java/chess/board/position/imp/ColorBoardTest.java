package chess.board.position.imp;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.board.Color;
import chess.board.Piece;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.debug.chess.ColorBoardDebug;
import chess.board.representations.fen.FENDecoder;
import chess.board.iterators.SquareIterator;
import chess.board.position.PiecePlacement;


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
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}	

}
