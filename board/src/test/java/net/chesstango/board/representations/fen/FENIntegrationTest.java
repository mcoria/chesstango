package net.chesstango.board.representations.fen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

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
		
		String fen = coder.getChessRepresentation();
		
		assertEquals(FENDecoder.INITIAL_FEN, fen);
		
	}	

}
