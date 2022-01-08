package chess.positioncaptures;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.Color;
import chess.Square;
import chess.builder.ChessBuilderParts;
import chess.debug.builder.DebugChessFactory;
import chess.layers.PosicionPiezaBoard;
import chess.parsers.FENParser;
import chess.positioncaptures.ImprovedCapturer;

/**
 * @author Mauricio Coria
 *
 */
public class ImprovedCapturerTest {
	
	@Test
	public void testPositionCapturedByPeonBlanco() {
		PosicionPiezaBoard dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPeonNegro() {
		PosicionPiezaBoard dummyBoard = getTablero("8/8/8/1p6/8/8/8/8");
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.a4) );
		assertFalse( capturer.positionCaptured(Color.BLACK, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByCaballo() {
		PosicionPiezaBoard dummyBoard = getTablero("8/8/8/3N4/8/8/8/8");
		
		ImprovedCapturer capturer = new ImprovedCapturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c7) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.e7) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.f6) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.f4) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c3) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.e3) );		
	}	
	
	private PosicionPiezaBoard getTablero(String string) {		
		ChessBuilderParts builder = new ChessBuilderParts(new DebugChessFactory());
		FENParser parser = new FENParser(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPosicionPiezaBoard();
	}		
}
