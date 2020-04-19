package chess;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import parsers.FENBoarBuilder;

public class BoardTest01 {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void test01() {
		Board tablero = builder.withFEN("r1bqkb1r/pppp1Qpp/2n4n/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1").buildBoard();
		
		assertEquals(Color.NEGRO, tablero.getBoardState().getTurnoActual());
		assertTrue(tablero.isKingInCheck());
		assertEquals(1, tablero.getLegalMoves().size());
	}
}
