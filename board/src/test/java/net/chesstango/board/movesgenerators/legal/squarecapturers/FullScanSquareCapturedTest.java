package net.chesstango.board.movesgenerators.legal.squarecapturers;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.builders.PiecePlacementBuilder;
import net.chesstango.board.debug.builder.ChessFactoryDebug;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.position.Board;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 *
 */
public class FullScanSquareCapturedTest {
	
	@Test
	public void testPositionCapturedByPawnWhite() {
		Board dummyBoard = getTablero("8/8/8/1P6/8/8/8/8");
		
		FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.a6) );
		assertFalse( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b6) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c6) );
	}
	
	@Test
	public void testPositionCapturedByPawnBlack() {
		Board dummyBoard = getTablero("8/8/8/1p6/8/8/8/8");
		
		FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.BLACK, Square.a4) );
		assertFalse( fullScanSquareCapturer.isCaptured(Color.BLACK, Square.b4) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.BLACK, Square.c4) );
	}	

	
	@Test
	public void testPositionCapturedByKnight() {
		Board dummyBoard = getTablero("8/8/8/3N4/8/8/8/8");
		
		FullScanSquareCaptured fullScanSquareCapturer = new FullScanSquareCaptured(dummyBoard);
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c7) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.e7) );
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b6) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.f6) );
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.b4) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.f4) );
		
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.c3) );
		assertTrue( fullScanSquareCapturer.isCaptured(Color.WHITE, Square.e3) );
	}	
	
	private Board getTablero(String string) {
		ChessFactory chessFactory = new ChessFactoryDebug();
		
		PiecePlacementBuilder builder = new PiecePlacementBuilder(chessFactory);
		
		FENDecoder parser = new FENDecoder(builder);
		
		parser.parsePiecePlacement(string);
		
		return builder.getChessRepresentation();
	}		
}
