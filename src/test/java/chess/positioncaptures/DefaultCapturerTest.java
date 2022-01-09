package chess.positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.Color;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.ColorBoard;
import chess.layers.PiecePlacement;
import chess.parsers.FENParser;
import chess.positioncaptures.IteratorCapturer;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class DefaultCapturerTest {

	
	@Test
	public void testPositionCaptured() {
		PiecePlacement dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		ColorBoard colorBoard = new ColorBoard(dummyBoard);
		
		MoveGeneratorStrategy moveGeneratorStrategy = new MoveGeneratorStrategy();
		moveGeneratorStrategy.setDummyBoard(dummyBoard);
		moveGeneratorStrategy.setColorBoard(colorBoard);
		
		IteratorCapturer capturer = new IteratorCapturer(dummyBoard, colorBoard, moveGeneratorStrategy);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c6) );
	}
	
	private PiecePlacement getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}		

}
