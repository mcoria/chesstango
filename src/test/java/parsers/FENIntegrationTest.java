package parsers;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class FENIntegrationTest {

	@Test
	public void testTurnoBlanco() {
		FENCoder coder = new FENCoder();
		
		FENParser parser = new FENParser(coder);
		
		parser.parseFEN(FENParser.INITIAL_FEN);
		
		String fen = coder.getFEN();
		
		assertEquals(FENParser.INITIAL_FEN, fen);
		
	}	

}
