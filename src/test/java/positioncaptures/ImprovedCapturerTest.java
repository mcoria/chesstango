package positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.Color;
import chess.Square;
import layers.PosicionPiezaBoard;
import parsers.FENBoarBuilder;

public class ImprovedCapturerTest {
	private FENBoarBuilder<ChessBuilder> builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
	}
	
	@Test
	public void testPositionCapturedByPeonBlanco() {
		PosicionPiezaBoard dummyBoard =  builder.constructTablero("8/8/8/1P6/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.BLANCO, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.BLANCO, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPeonNegro() {
		PosicionPiezaBoard dummyBoard =   builder.constructTablero("8/8/8/1p6/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.NEGRO, Square.a4) );
		assertFalse( capturer.positionCaptured(Color.NEGRO, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.NEGRO, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByCaballo() {
		PosicionPiezaBoard dummyBoard =  builder.constructTablero("8/8/8/3N4/8/8/8/8").getBuilder().getPosicionPiezaBoard();
		
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
