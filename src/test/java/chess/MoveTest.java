package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import chess.Move.MoveType;
import parsers.FENBoarBuilder;

public class MoveTest {

	private FENBoarBuilder builder;

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testEquals01() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);
		
		assertEquals(new Move(origen, destino, MoveType.SIMPLE), new Move(origen, destino, MoveType.SIMPLE));
	}
	
	@Test
	public void testToString01() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);
		Move move = new Move(origen, destino, MoveType.SIMPLE);
		assertEquals("e5=TORRE_BLANCO e7=null; SIMPLE", move.toString());
	}
	
	@Test
	public void testToString02() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);
		Move move = new Move(origen, destino, null);
		assertEquals("e5=TORRE_BLANCO e7=null; ERROR", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		Map.Entry<Square, Pieza> a2 = new SimpleImmutableEntry<Square, Pieza>(Square.a2, null);
		Map.Entry<Square, Pieza> a3 = new SimpleImmutableEntry<Square, Pieza>(Square.a3, null);
		Map.Entry<Square, Pieza> a4 = new SimpleImmutableEntry<Square, Pieza>(Square.a4, null);
		Map.Entry<Square, Pieza> b1 = new SimpleImmutableEntry<Square, Pieza>(Square.b1, null);
		Map.Entry<Square, Pieza> b2 = new SimpleImmutableEntry<Square, Pieza>(Square.b2, null);
		Map.Entry<Square, Pieza> b3 = new SimpleImmutableEntry<Square, Pieza>(Square.b3, null);
		
		
		Move move1 = new Move(a2, a3, null);
		Move move2 = new Move(a2, a4, null);
		Move move3 = new Move(b2, b3, null);
		Move move4 = new Move(b1, a3, null);
		
		assertTrue(move1.compareTo(move2) > 0);
		assertTrue(move1.compareTo(move3) > 0);
		assertTrue(move1.compareTo(move4) > 0);
		
		assertTrue(move2.compareTo(move3) > 0);
		
		assertTrue(move3.compareTo(move4) > 0);

	}	
	
	
	@Test
	public void testSimple() {
		DummyBoard tablero = builder.withFEN("8/8/8/4R3/8/8/8/8 w KQkq - 0 1").buildDummyBoard();
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertTrue(tablero.isEmtpy(Square.e7));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);

		Move move = new Move(origen, destino, MoveType.SIMPLE);
		
		move.execute(tablero);
		
		assertEquals(tablero.getPieza(Square.e7), Pieza.TORRE_BLANCO);
		assertTrue(tablero.isEmtpy(Square.e5));
		assertNull(tablero.getBoardState().getPeonPasanteSquare());
		
		
		move.undo(tablero);
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertTrue(tablero.isEmtpy(Square.e7));
		assertNull(tablero.getBoardState().getPeonPasanteSquare());
	}
	
	@Test
	public void testCapture() {
		DummyBoard tablero = builder.withFEN("8/4p3/8/4R3/8/8/8/8 w KQkq - 0 1").buildDummyBoard();
		
		assertEquals(tablero.getPieza(Square.e5), Pieza.TORRE_BLANCO);
		assertEquals(tablero.getPieza(Square.e7), Pieza.PEON_NEGRO);
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, Pieza.PEON_NEGRO);

		
		Move move = new Move(origen, destino, MoveType.CAPTURA);
		
		move.execute(tablero);
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e7));
		assertTrue(tablero.isEmtpy(Square.e5));
		
		move.undo(tablero);
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e7));
		
	}

}
