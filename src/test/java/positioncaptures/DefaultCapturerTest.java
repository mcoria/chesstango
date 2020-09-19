package positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Square;
import layers.ColorBoard;
import layers.DummyBoard;
import movegenerators.MoveGeneratorStrategy;
import parsers.FENBoarBuilder;

public class DefaultCapturerTest {
	private FENBoarBuilder builder;


	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testPositionCaptured() {
		DummyBoard dummyBoard = builder.withTablero("8/8/8/1P6/8/8/8/8").buildDummyBoard();
		MoveGeneratorStrategy strategy = builder.buildMoveGeneratorStrategy();
		ColorBoard colorBoard = builder.buildColorBoard();
		
		DefaultCapturer capturer = new DefaultCapturer(dummyBoard, colorBoard, strategy);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c6) );
	}

}
