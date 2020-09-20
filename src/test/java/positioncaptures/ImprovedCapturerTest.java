package positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Square;
import layers.DummyBoard;
import parsers.FENBoarBuilder;

public class ImprovedCapturerTest {
	private FENBoarBuilder builder;


	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testPositionCapturedByPeonBlanco() {
		DummyBoard dummyBoard = builder.withTablero("8/8/8/1P6/8/8/8/8").buildDummyBoard();
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPeonNegro() {
		DummyBoard dummyBoard = builder.withTablero("8/8/8/1p6/8/8/8/8").buildDummyBoard();
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.NEGRO, Square.a4) );
		assertFalse( capturer.positionCaptured(Color.NEGRO, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.NEGRO, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByCaballo() {
		DummyBoard dummyBoard = builder.withTablero("8/8/8/3N4/8/8/8/8").buildDummyBoard();
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c7) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.e7) );
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.f6) );
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.f4) );
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c3) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.e3) );		
		
	}		
}
