package chess.board.legalmovesgenerators.squarecapturers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import chess.board.Color;
import chess.board.Square;
import chess.board.legalmovesgenerators.squarecapturers.FullScanSquareCapturer;
import chess.board.builder.imp.PiecePlacementBuilder;
import chess.board.debug.builder.ChessFactoryDebug;
import chess.board.factory.ChessFactory;
import chess.board.fen.FENDecoder;
import chess.board.position.PiecePlacement;

/**
 * @author Mauricio Coria
 *
 */
public class FullScanSquareCapturerTest {
	
	@Test
	public void testPositionCapturedByPawnWhite() {
		PiecePlacement dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		
		FullScanSquareCapturer fullScanSquareCapturer = new FullScanSquareCapturer(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.a6) );
		assertFalse( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPawnBlack() {
		PiecePlacement dummyBoard = getTablero("8/8/8/1p6/8/8/8/8");
		
		FullScanSquareCapturer fullScanSquareCapturer = new FullScanSquareCapturer(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.BLACK, Square.a4) );
		assertFalse( fullScanSquareCapturer.positionCaptured(Color.BLACK, Square.b4) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.BLACK, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByKnight() {
		PiecePlacement dummyBoard = getTablero("8/8/8/3N4/8/8/8/8");
		
		FullScanSquareCapturer fullScanSquareCapturer = new FullScanSquareCapturer(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.c7) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.e7) );
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.b6) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.f6) );
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.b4) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.f4) );
		
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.c3) );
		assertTrue( fullScanSquareCapturer.positionCaptured(Color.WHITE, Square.e3) );
	}	
	
	private PiecePlacement getTablero(String string) {
		ChessFactory chessFactory = new ChessFactoryDebug();
		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(chessFactory);
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getResult();
	}		
}
