package chess.analyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.Color;
import chess.Square;
import chess.analyzer.Capturer;
import chess.builder.ChessPositionBuilderImp;
import chess.debug.builder.DebugChessFactory;
import chess.fen.FENDecoder;
import chess.position.PiecePlacement;

/**
 * @author Mauricio Coria
 *
 */
public class CapturerTest {
	
	@Test
	public void testPositionCapturedByPawnWhite() {
		PiecePlacement dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		
		Capturer capturer = new Capturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.a6) );
		assertFalse( capturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( capturer.positionCaptured(Color.WHITE, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPawnBlack() {
		PiecePlacement dummyBoard = getTablero("8/8/8/1p6/8/8/8/8");
		
		Capturer capturer = new Capturer(dummyBoard);
		
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.a4) );
		assertFalse( capturer.positionCaptured(Color.BLACK, Square.b4) );
		assertTrue( capturer.positionCaptured(Color.BLACK, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByKnight() {
		PiecePlacement dummyBoard = getTablero("8/8/8/3N4/8/8/8/8");
		
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
	
	private PiecePlacement getTablero(String string) {		
		ChessPositionBuilderImp builder = new ChessPositionBuilderImp(new DebugChessFactory());
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getPiecePlacement();
	}		
}
