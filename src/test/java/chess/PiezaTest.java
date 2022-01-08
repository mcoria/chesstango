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
		assertEquals(Color.WHITE, Pieza.PAWN_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.ROOK_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.KNIGHT_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.BISHOP_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.QUEEN_WHITE.getColor());
		assertEquals(Color.WHITE, Pieza.KING_WHITE.getColor());
	}
	
	@Test
	public void testNegro() {
		assertEquals(Color.BLACK, Pieza.PAWN_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.ROOK_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.KNIGHT_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.BISHOP_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.QUEEN_BLACK.getColor());
		assertEquals(Color.BLACK, Pieza.KING_BLACK.getColor());
	}	

}
