package chess;

import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Mauricio Coria
 *
 */
public class PieceTest {

	@Test
	public void testBlanco() {
		assertEquals(Color.WHITE, Piece.PAWN_WHITE.getColor());
		assertEquals(Color.WHITE, Piece.ROOK_WHITE.getColor());
		assertEquals(Color.WHITE, Piece.KNIGHT_WHITE.getColor());
		assertEquals(Color.WHITE, Piece.BISHOP_WHITE.getColor());
		assertEquals(Color.WHITE, Piece.QUEEN_WHITE.getColor());
		assertEquals(Color.WHITE, Piece.KING_WHITE.getColor());
	}
	
	@Test
	public void testNegro() {
		assertEquals(Color.BLACK, Piece.PAWN_BLACK.getColor());
		assertEquals(Color.BLACK, Piece.ROOK_BLACK.getColor());
		assertEquals(Color.BLACK, Piece.KNIGHT_BLACK.getColor());
		assertEquals(Color.BLACK, Piece.BISHOP_BLACK.getColor());
		assertEquals(Color.BLACK, Piece.QUEEN_BLACK.getColor());
		assertEquals(Color.BLACK, Piece.KING_BLACK.getColor());
	}	

}