package net.chesstango.board.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * @author Mauricio Coria
 *
 */
public class AbstractMoveTest {

	private MoveFactory moveFactory;

	@Before
	public void setup(){
		moveFactory = SingletonMoveFactories.getDefaultMoveFactoryWhite();
	}
	
	@Test
	public void testEquals01() {
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);
		
		assertEquals(moveFactory.createSimpleMove(origen, destino), moveFactory.createSimpleMove(origen, destino));
	}
	
	@Test
	@Ignore
	public void testToString01() {
		PiecePositioned origen = PiecePositioned.getPiecePositioned(Square.e5, Piece.ROOK_WHITE);
		PiecePositioned destino = PiecePositioned.getPiecePositioned(Square.e7, null);
		Move move = moveFactory.createSimpleMove(origen, destino);
		assertEquals("e5=ROOK_WHITE e7=null - SimpleMove", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		PiecePositioned a2 = PiecePositioned.getPiecePositioned(Square.a2, Piece.PAWN_WHITE);
		PiecePositioned a3 = PiecePositioned.getPiecePositioned(Square.a3, null);
		PiecePositioned a4 = PiecePositioned.getPiecePositioned(Square.a4, Piece.ROOK_WHITE);
		PiecePositioned b1 = PiecePositioned.getPiecePositioned(Square.b1, Piece.QUEEN_WHITE);
		PiecePositioned b2 = PiecePositioned.getPiecePositioned(Square.b2, Piece.PAWN_WHITE);
		PiecePositioned b3 = PiecePositioned.getPiecePositioned(Square.b3, null);
		
		
		Move move1 = moveFactory.createSimpleMove(a2, a3);
		Move move2 = moveFactory.createSimpleMove(a2, a4);
		Move move3 = moveFactory.createSimpleMove(b2, b3);
		Move move4 = moveFactory.createSimpleMove(b1, a3);
		
		assertTrue(move1.compareTo(move2) > 0);
		assertTrue(move1.compareTo(move3) > 0);
		assertTrue(move1.compareTo(move4) > 0);
		
		assertTrue(move2.compareTo(move3) > 0);
		
		assertTrue(move3.compareTo(move4) > 0);
	}

}
