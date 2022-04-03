package chess.board.moves.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.moves.Move;
import chess.board.moves.imp.AbstractMove;
import chess.board.moves.imp.SimpleMove;


/**
 * @author Mauricio Coria
 *
 */
public class AbstractMoveTest {

	@Before
	public void setUp() throws Exception {
		//builder = new FENBoarBuilder();
	}
	
	@Test
	public void testEquals01() {
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);
		
		assertEquals(new SimpleMove(origen, destino), new SimpleMove(origen, destino));
	}
	
	@Test
	public void testToString01() {
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);
		Move move = new SimpleMove(origen, destino);
		assertEquals("e5=ROOK_WHITE e7=null - SimpleMove", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		PiecePositioned a2 = PiecePositioned.getPiecePositioned(Square.a2, null);
		PiecePositioned a3 = PiecePositioned.getPiecePositioned(Square.a3, null);
		PiecePositioned a4 = PiecePositioned.getPiecePositioned(Square.a4, null);
		PiecePositioned b1 = PiecePositioned.getPiecePositioned(Square.b1, null);
		PiecePositioned b2 = PiecePositioned.getPiecePositioned(Square.b2, null);
		PiecePositioned b3 = PiecePositioned.getPiecePositioned(Square.b3, null);
		
		
		AbstractMove move1 = new SimpleMove(a2, a3);
		AbstractMove move2 = new SimpleMove(a2, a4);
		AbstractMove move3 = new SimpleMove(b2, b3);
		AbstractMove move4 = new SimpleMove(b1, a3);
		
		assertTrue(move1.compareTo(move2) > 0);
		assertTrue(move1.compareTo(move3) > 0);
		assertTrue(move1.compareTo(move4) > 0);
		
		assertTrue(move2.compareTo(move3) > 0);
		
		assertTrue(move3.compareTo(move4) > 0);

	}

}
