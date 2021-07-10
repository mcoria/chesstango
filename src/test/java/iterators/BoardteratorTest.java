package iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import builder.ChessBuilderParts;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import parsers.FENParser;

public class BoardteratorTest {

	private FENParser parser = null;
	
	private ChessBuilderParts builder = null;

	@Before
	public void setUp() throws Exception {
		builder = new ChessBuilderParts();
		parser = new FENParser(builder);
	}

	@Test
	public void testTopDownSquareIterator() {
		parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		PosicionPiezaBoard tablero =  builder.getPosicionPiezaBoard();
		
		Iterator<PosicionPieza> iterator = tablero.iterator(new TopDownSquareIterator());
		
		PosicionPieza entry =  null;
		
		// Rank 8
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a8, entry.getKey());
		assertEquals(Pieza.TORRE_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b8, entry.getKey());
		assertEquals(Pieza.CABALLO_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c8, entry.getKey());
		assertEquals(Pieza.ALFIL_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d8, entry.getKey());
		assertEquals(Pieza.REINA_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e8, entry.getKey());
		assertEquals(Pieza.REY_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f8, entry.getKey());
		assertEquals(Pieza.ALFIL_NEGRO, entry.getValue());		
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g8, entry.getKey());
		assertEquals(Pieza.CABALLO_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h8, entry.getKey());
		assertEquals(Pieza.TORRE_NEGRO, entry.getValue());
		
		// Rank 7		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());
		
		
		// Rank 6		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h6, entry.getKey());
		assertNull(entry.getValue());		
		
		// Rank 5		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h5, entry.getKey());
		assertNull(entry.getValue());
		
		// Rank 4
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h4, entry.getKey());
		assertNull(entry.getValue());		
		
		// Rank 3
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h3, entry.getKey());
		assertNull(entry.getValue());
		
		// Rank 2
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());		
		
		// Rank 1
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a1, entry.getKey());
		assertEquals(Pieza.TORRE_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b1, entry.getKey());
		assertEquals(Pieza.CABALLO_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c1, entry.getKey());
		assertEquals(Pieza.ALFIL_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d1, entry.getKey());
		assertEquals(Pieza.REINA_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e1, entry.getKey());
		assertEquals(Pieza.REY_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f1, entry.getKey());
		assertEquals(Pieza.ALFIL_BLANCO, entry.getValue());		
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g1, entry.getKey());
		assertEquals(Pieza.CABALLO_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h1, entry.getKey());
		assertEquals(Pieza.TORRE_BLANCO, entry.getValue());
		
		//END
		assertFalse(iterator.hasNext());		
	}

	@Test
	public void testBottomUpSquareIterator() {
		parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		PosicionPiezaBoard tablero =  builder.getPosicionPiezaBoard();
		
		Iterator<PosicionPieza> iterator = tablero.iterator(new BottomUpSquareIterator());
		
		PosicionPieza entry =  null;
		
		// Rank 1
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a1, entry.getKey());
		assertEquals(Pieza.TORRE_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b1, entry.getKey());
		assertEquals(Pieza.CABALLO_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c1, entry.getKey());
		assertEquals(Pieza.ALFIL_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d1, entry.getKey());
		assertEquals(Pieza.REINA_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e1, entry.getKey());
		assertEquals(Pieza.REY_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f1, entry.getKey());
		assertEquals(Pieza.ALFIL_BLANCO, entry.getValue());		
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g1, entry.getKey());
		assertEquals(Pieza.CABALLO_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h1, entry.getKey());
		assertEquals(Pieza.TORRE_BLANCO, entry.getValue());
		
		
		// Rank 2
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());	
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h2, entry.getKey());
		assertEquals(Pieza.PEON_BLANCO, entry.getValue());
		
		// Rank 3
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g3, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h3, entry.getKey());
		assertNull(entry.getValue());
		
		// Rank 4
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g4, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h4, entry.getKey());
		assertNull(entry.getValue());
		
		
		// Rank 5		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g5, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h5, entry.getKey());
		assertNull(entry.getValue());	
		
		// Rank 6		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g6, entry.getKey());
		assertNull(entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h6, entry.getKey());
		assertNull(entry.getValue());			
		
		// Rank 7		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());	
			
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h7, entry.getKey());
		assertEquals(Pieza.PEON_NEGRO, entry.getValue());
		
		// Rank 8
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.a8, entry.getKey());
		assertEquals(Pieza.TORRE_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.b8, entry.getKey());
		assertEquals(Pieza.CABALLO_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.c8, entry.getKey());
		assertEquals(Pieza.ALFIL_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.d8, entry.getKey());
		assertEquals(Pieza.REINA_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.e8, entry.getKey());
		assertEquals(Pieza.REY_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.f8, entry.getKey());
		assertEquals(Pieza.ALFIL_NEGRO, entry.getValue());		
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.g8, entry.getKey());
		assertEquals(Pieza.CABALLO_NEGRO, entry.getValue());
		
		assertTrue(iterator.hasNext());
		entry = iterator.next();
		assertEquals(Square.h8, entry.getKey());
		assertEquals(Pieza.TORRE_NEGRO, entry.getValue());
		
		//END
		assertFalse(iterator.hasNext());		
	}	
}
