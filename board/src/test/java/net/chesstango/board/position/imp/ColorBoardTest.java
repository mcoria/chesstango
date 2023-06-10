package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.debug.chess.ColorBoardDebug;
import net.chesstango.board.iterators.SquareIterator;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 *
 */
public class ColorBoardTest {

	private ColorBoardImp colorBoard;

	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		SquareBoard tablero = getTablero("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		colorBoard = new ColorBoardDebug();
		colorBoard.init(tablero);
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.WHITE); iterator.hasNext();) {
			Piece piece = tablero.getPiece(iterator.next());
			assertEquals(Color.WHITE, piece.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}
	
	
	private SquareBoard getTablero(String string) {
		PiecePlacementBuilder builder = new PiecePlacementBuilder(new ChessFactoryDebug());

		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}	

}
