package chess.parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.parsers.FENCoder;
import chess.parsers.FENParser;

/**
 * @author Mauricio Coria
 *
 */
public class FENIntegrationTest {

	@Test
	public void testTurnoWhite() {
		FENCoder coder = new FENCoder();
		
		FENParser parser = new FENParser(coder);
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		String fen = coder.getResult();
		
		assertEquals(FENParser.INITIAL_FEN, fen);
		
	}	

}
