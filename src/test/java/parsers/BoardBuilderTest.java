package parsers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilder;
import chess.Color;
import chess.Board;
import chess.Square;
import parsers.FENBoarBuilder;

public class BoardBuilderTest {

	private FENBoarBuilder<ChessBuilder> builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder<ChessBuilder>(new ChessBuilder());
	}
	
	@Test
	public void testParse() {
		Board board =  builder.constructFEN("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1").getBuilder().buildBoard();
		
		assertEquals(Color.NEGRO, board.getBoardState().getTurnoActual());
		assertEquals(Square.e3, board.getBoardState().getPeonPasanteSquare());
		assertTrue(board.getBoardState().isEnroqueBlancoReyPermitido());
		assertTrue(board.getBoardState().isEnroqueBlancoReinaPermitido());
		assertTrue(board.getBoardState().isEnroqueNegroReyPermitido());
		assertTrue(board.getBoardState().isEnroqueNegroReinaPermitido());
	}	

}
