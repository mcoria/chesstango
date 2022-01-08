package chess;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Mauricio Coria
 *
 */
public class PiezaTest {

	@Test
	public void testBlanco() {
		assertEquals(Color.WHITE, Pieza.PEON_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.TORRE_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.CABALLO_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.ALFIL_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.QUEEN_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.KING_WHITE.getColor());
	}
	
	@Test
	public void testNegro() {
		assertEquals(Color.BLACK, Pieza.PEON_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.TORRE_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.CABALLO_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.ALFIL_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.QUEEN_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.KING_BLACK.getColor());
	}	

}
