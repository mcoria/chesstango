package parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.Board;
import chess.DummyBoard;
import chess.Pieza;

public class FENCoderTest {

	@Test
	public void testCodePiecePlacement01() {
		FENCoder coder = new FENCoder();
		
		String actual = coder.codePiecePlacement(new Pieza[]{Pieza.ALFIL_BLANCO,null,null, Pieza.PEON_BLANCO, null, Pieza.ALFIL_BLANCO,null, Pieza.PEON_BLANCO});
		
		assertEquals("B2P1B1P", actual);
	}
	
	@Test
	public void testCodePiecePlacement02() {
		FENCoder coder = new FENCoder();
		
		String actual = coder.codePiecePlacement(new Pieza[]{null, null, null, null, null, null, null, null});
		
		assertEquals("8", actual);
	}	
	
	
	@Test
	public void testCodePiecePlacement03() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		FENCoder coder = new FENCoder();
		String actual = coder.codePiecePlacement(tablero);
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", actual);		
	}

	@Test
	public void testCodeBoard01() {
		FENParser parser = new FENParser();
		Board tablero = parser.parse("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
		FENCoder coder = new FENCoder();
		String actual = coder.code(tablero);
		
		assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", actual);		
	}
	
	@Test
	public void testCodeBoard02() {
		FENParser parser = new FENParser();
		Board tablero = parser.parse("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1");
		FENCoder coder = new FENCoder();
		String actual = coder.code(tablero);
		
		assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", actual);		
	}	
}
