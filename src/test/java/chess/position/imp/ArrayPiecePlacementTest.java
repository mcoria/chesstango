package chess.position.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;
import chess.iterators.pieceplacement.BoardBitIterator;
import chess.iterators.pieceplacement.PiecePlacementIterator;


/**
 * @author Mauricio Coria
 *
 */
public class ArrayPiecePlacementTest {

	@Test
	public void test() {
		ArrayPiecePlacement tablero = new ArrayPiecePlacement();
		
		tablero.setPieza(Square.a1, Piece.ROOK_WHITE);
		tablero.setPieza(Square.b7, Piece.PAWN_BLACK);
		tablero.setPieza(Square.b8, Piece.KNIGHT_BLACK);
		tablero.setPieza(Square.e1, Piece.KING_WHITE);
		tablero.setPieza(Square.e8, Piece.KING_BLACK);
		
		
		// Al position should be not NULL (including emtpy squares)
		assertNotNull(tablero.getPosicion(Square.a1));
		assertNotNull(tablero.getPosicion(Square.b7));
		assertNotNull(tablero.getPosicion(Square.b8));
		assertNotNull(tablero.getPosicion(Square.e1));
		assertNotNull(tablero.getPosicion(Square.e8));
		assertNotNull(tablero.getPosicion(Square.e3));
		
		
		
		long posiciones = 0;
		posiciones |= Square.a1.getPosicion();
		posiciones |= Square.b7.getPosicion();
		posiciones |= Square.b8.getPosicion();
		posiciones |= Square.e1.getPosicion();
		posiciones |= Square.e8.getPosicion();
		posiciones |= Square.e3.getPosicion();
		
		List<PiecePositioned> posicionesList = new ArrayList<PiecePositioned>();

		for (PiecePlacementIterator iterator =  new BoardBitIterator(tablero.tablero, posiciones); iterator.hasNext();) {
			posicionesList.add(iterator.next());
		}
		

		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.a1, Piece.ROOK_WHITE)));
		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.b7, Piece.PAWN_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.b8, Piece.KNIGHT_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e1, Piece.KING_WHITE)));
		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e8, Piece.KING_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.getPiecePositioned(Square.e3, null)));
		assertEquals(6, posicionesList.size());

	}
	
	@Test
	public void testToString() {
		ArrayPiecePlacement tablero = new ArrayPiecePlacement();
		
		assertTrue(tablero.toString().length() > 0);

	}	

}
