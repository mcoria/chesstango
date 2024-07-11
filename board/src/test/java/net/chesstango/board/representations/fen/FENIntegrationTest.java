package net.chesstango.board.representations.fen;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		
		String fen = coder.getChessRepresentation().toString();
		
		assertEquals(FENDecoder.INITIAL_FEN, fen);
		
	}	

}
