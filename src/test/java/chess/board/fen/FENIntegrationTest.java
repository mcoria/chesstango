package chess.board.fen;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import chess.board.fen.FENDecoder;
import chess.board.fen.FENEncoder;

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
