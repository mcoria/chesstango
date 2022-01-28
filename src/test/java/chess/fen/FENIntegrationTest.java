package chess.fen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.fen.FENEncoder;
import chess.fen.FENDecoder;

/**
 * @author Mauricio Coria
 *
 */
public class FENIntegrationTest {

	@Test
	public void testTurnoWhite() {
		FENEncoder coder = new FENEncoder();
		
		FENDecoder parser = new FENDecoder(coder);
		
		parser.parseFEN(FENDecoder.INITIAL_FEN);
		
		String fen = coder.getResult();
		
		assertEquals(FENDecoder.INITIAL_FEN, fen);
		
	}	

}
