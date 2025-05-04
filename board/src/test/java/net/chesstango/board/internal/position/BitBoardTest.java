package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.iterators.bysquare.SquareIterator;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENExporter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


/**
 * @author Mauricio Coria
 *
 */
public class BitBoardTest {

	private BitBoardImp colorBoard;

	
	@Test
	public void test01() {
		int totalPiezas = 0;
		
		SquareBoard tablero = getTablero("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR  w KQkq - 0 1");
		
		colorBoard = new BitBoardDebug();
		colorBoard.init(tablero);
		
		for (SquareIterator iterator = colorBoard.iteratorSquare(Color.WHITE); iterator.hasNext();) {
			Piece piece = tablero.getPiece(iterator.next());
			assertEquals(Color.WHITE, piece.getColor());
			totalPiezas++;
		}
		assertEquals(16, totalPiezas);
	}
	
	
	private SquareBoard getTablero(String string) {
		SquareBoardBuilder builder = new SquareBoardBuilder();

		FENExporter exporter = new FENExporter(builder);

		exporter.export(FEN.of(string));

		return builder.getPositionRepresentation();
	}	

}
