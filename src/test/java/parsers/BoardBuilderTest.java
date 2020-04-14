package parsers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import chess.Color;
import chess.Board;
import chess.Square;
import parsers.FENBoarBuilder;

public class BoardBuilderTest {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testParse() {
		Board board = builder.withFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1").buildDummyBoard();
		
		assertEquals(Color.NEGRO, board.getBoardState().getTurnoActual());
		assertEquals(Square.e3, board.getBoardState().getPeonPasanteSquare());
		assertTrue(board.getBoardState().isEnroqueBlancoReyPermitido());
		assertTrue(board.getBoardState().isEnroqueBlancoReinaPermitido());
		assertTrue(board.getBoardState().isEnroqueNegroReyPermitido());
		assertTrue(board.getBoardState().isEnroqueNegroReinaPermitido());
	}	

}
