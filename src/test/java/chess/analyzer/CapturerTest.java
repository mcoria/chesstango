package chess.analyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.Color;
import chess.Square;
import chess.builder.imp.PiecePlacementBuilder;
import chess.debug.builder.DebugChessFactory;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;
import chess.fen.FENDecoder;
import chess.position.ChessPosition;

/**
 * @author Mauricio Coria
 *
 */
public class CapturerTest {
	
	@Test
	public void testPositionCapturedByPawnWhite() {
		ChessPosition dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		
		Capturer capturer = new Capturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPawnBlack() {
		ChessPosition dummyBoard = getTablero("8/8/8/1p6/8/8/8/8");
		
		Capturer capturer = new Capturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.a4) );
		assertFalse( capturer.positionCaptured(Color.BLACK, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByKnight() {
		ChessPosition dummyBoard = getTablero("8/8/8/3N4/8/8/8/8");
		
		Capturer capturer = new Capturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c7) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.e7) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.f6) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.f4) );
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c3) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.e3) );		
	}	
	
	private ChessPosition getTablero(String string) {
		ChessFactory chessFactory = new DebugChessFactory();
		
		ChessInjector injector = new ChessInjector(chessFactory);
		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(injector);
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return injector.getChessPosition();
	}		
}
