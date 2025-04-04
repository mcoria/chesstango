package net.chesstango.board.position.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.byposition.BitIterator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;




/**
 * @author Mauricio Coria
 *
 */
public class SquareBoardImpTest {

	@Test
	public void test() {
		SquareBoardImp tablero = new SquareBoardImp();
		
		tablero.setPiece(Square.a1, Piece.ROOK_WHITE);
		tablero.setPiece(Square.b7, Piece.PAWN_BLACK);
		tablero.setPiece(Square.b8, Piece.KNIGHT_BLACK);
		tablero.setPiece(Square.e1, Piece.KING_WHITE);
		tablero.setPiece(Square.e8, Piece.KING_BLACK);
		
		
		// Al position should be not NULL (including emtpy squares)
		assertNotNull(tablero.getPosition(Square.a1));
		assertNotNull(tablero.getPosition(Square.b7));
		assertNotNull(tablero.getPosition(Square.b8));
		assertNotNull(tablero.getPosition(Square.e1));
		assertNotNull(tablero.getPosition(Square.e8));
		assertNotNull(tablero.getPosition(Square.e3));
		
		
		
		long posiciones = 0;
		posiciones |= Square.a1.getBitPosition();
		posiciones |= Square.b7.getBitPosition();
		posiciones |= Square.b8.getBitPosition();
		posiciones |= Square.e1.getBitPosition();
		posiciones |= Square.e8.getBitPosition();
		posiciones |= Square.e3.getBitPosition();
		
		List<PiecePositioned> posicionesList = new ArrayList<>();

		for (Iterator<PiecePositioned> iterator = new BitIterator<>(tablero, posiciones); iterator.hasNext();) {
			posicionesList.add(iterator.next());
		}
		

		assertTrue(posicionesList.contains(PiecePositioned.of(Square.a1, Piece.ROOK_WHITE)));
		assertTrue(posicionesList.contains(PiecePositioned.of(Square.b7, Piece.PAWN_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.of(Square.b8, Piece.KNIGHT_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.of(Square.e1, Piece.KING_WHITE)));
		assertTrue(posicionesList.contains(PiecePositioned.of(Square.e8, Piece.KING_BLACK)));
		assertTrue(posicionesList.contains(PiecePositioned.of(Square.e3, null)));
		assertEquals(6, posicionesList.size());
	}

	
	@Test
	public void testToString() {
		SquareBoardImp tablero = new SquareBoardImp();
		
		assertTrue(tablero.toString().length() > 0);

	}	

}
