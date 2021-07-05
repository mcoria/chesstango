package positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.Color;
import chess.Square;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;
import movegenerators.MoveGeneratorStrategy;
import parsers.FENBoarBuilder;

public class DefaultCapturerTest {
	private FENBoarBuilder<ChessBuilder> builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
	}
	
	@Test
	public void testPositionCaptured() {
		PosicionPiezaBoard dummyBoard =  builder.constructTablero("8/8/8/1P6/8/8/8/8").getBuilder().buildPosicionPiezaBoard();
		MoveGeneratorStrategy strategy =  builder.getBuilder().buildMoveGeneratorStrategy();
		ColorBoard colorBoard = new ColorBoard(dummyBoard);
		
		IteratorCapturer capturer = new IteratorCapturer(dummyBoard, colorBoard, strategy);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c6) );
	}

}
