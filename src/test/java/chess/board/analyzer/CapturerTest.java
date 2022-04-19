package chess.board.analyzer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.board.Color;
import chess.board.Square;
import chess.board.analyzer.capturer.Capturer;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.factory.ChessFactory;
import chess.board.fen.FENDecoder;
import chess.board.position.PiecePlacement;

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
		ChessFactory chessFactory = new ChessFactoryDebug();
		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(chessFactory);
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}		
}
