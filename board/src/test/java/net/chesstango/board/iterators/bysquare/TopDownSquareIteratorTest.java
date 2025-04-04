package net.chesstango.board.iterators.bysquare;


import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.SquareBoardBuilder;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author Mauricio Coria
 *
 */
public class TopDownSquareIteratorTest {

	private FENDecoder parser = null;
	
	private SquareBoardBuilder builder = null;

	@BeforeEach
	public void setUp() throws Exception {
		builder = new SquareBoardBuilder();
		parser = new FENDecoder(builder);
	}

	@Test
	public void testTopDownSquareIterator() {
		parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		SquareBoard tablero =  builder.getChessRepresentation();

		Iterator<PiecePositioned> iterator = tablero.iterator(new TopDownSquareIterator());
		
		PiecePositioned entry =  null;
		
		// Rank 8
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a8, entry.getSquare());
		assertEquals(Piece.ROOK_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b8, entry.getSquare());
		assertEquals(Piece.KNIGHT_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c8, entry.getSquare());
		assertEquals(Piece.BISHOP_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d8, entry.getSquare());
		assertEquals(Piece.QUEEN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e8, entry.getSquare());
		assertEquals(Piece.KING_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f8, entry.getSquare());
		assertEquals(Piece.BISHOP_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g8, entry.getSquare());
		assertEquals(Piece.KNIGHT_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h8, entry.getSquare());
		assertEquals(Piece.ROOK_BLACK, entry.getPiece());
		
		// Rank 7		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h7, entry.getSquare());
		assertEquals(Piece.PAWN_BLACK, entry.getPiece());
		
		
		// Rank 6		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g6, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h6, entry.getSquare());
		assertNull(entry.getPiece());
		
		// Rank 5		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g5, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h5, entry.getSquare());
		assertNull(entry.getPiece());
		
		// Rank 4
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g4, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h4, entry.getSquare());
		assertNull(entry.getPiece());
		
		// Rank 3
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g3, entry.getSquare());
		assertNull(entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h3, entry.getSquare());
		assertNull(entry.getPiece());
		
		// Rank 2
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h2, entry.getSquare());
		assertEquals(Piece.PAWN_WHITE, entry.getPiece());
		
		// Rank 1
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a1, entry.getSquare());
		assertEquals(Piece.ROOK_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b1, entry.getSquare());
		assertEquals(Piece.KNIGHT_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c1, entry.getSquare());
		assertEquals(Piece.BISHOP_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d1, entry.getSquare());
		assertEquals(Piece.QUEEN_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e1, entry.getSquare());
		assertEquals(Piece.KING_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f1, entry.getSquare());
		assertEquals(Piece.BISHOP_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g1, entry.getSquare());
		assertEquals(Piece.KNIGHT_WHITE, entry.getPiece());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h1, entry.getSquare());
		assertEquals(Piece.ROOK_WHITE, entry.getPiece());
		
		//END
		assertFalse(iterator.hasNext());		
	}

}
