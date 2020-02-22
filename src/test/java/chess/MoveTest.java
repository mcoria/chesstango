package chess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import moveexecutors.AbstractMove;
import moveexecutors.CaptureMove;
import moveexecutors.SimpleMove;
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
		
		assertEquals(new SimpleMove(origen, destino), new SimpleMove(origen, destino));
	}
	
	@Test
	public void testToString01() {
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);
		Move move = new SimpleMove(origen, destino);
		assertEquals("e5=TORRE_BLANCO e7=null; SimpleMove", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		Map.Entry<Square, Pieza> a2 = new SimpleImmutableEntry<Square, Pieza>(Square.a2, null);
		Map.Entry<Square, Pieza> a3 = new SimpleImmutableEntry<Square, Pieza>(Square.a3, null);
		Map.Entry<Square, Pieza> a4 = new SimpleImmutableEntry<Square, Pieza>(Square.a4, null);
		Map.Entry<Square, Pieza> b1 = new SimpleImmutableEntry<Square, Pieza>(Square.b1, null);
		Map.Entry<Square, Pieza> b2 = new SimpleImmutableEntry<Square, Pieza>(Square.b2, null);
		Map.Entry<Square, Pieza> b3 = new SimpleImmutableEntry<Square, Pieza>(Square.b3, null);
		
		
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
	
	
	@Test
	public void testSimple() {
		DummyBoard tablero = builder.withFEN("8/8/8/4R3/8/8/8/8 w KQkq - 0 1").buildDummyBoard();
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertTrue(tablero.isEmtpy(Square.e7));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.e5, Pieza.TORRE_BLANCO);
		Map.Entry<Square, Pieza> destino = new SimpleImmutableEntry<Square, Pieza>(Square.e7, null);

		Move move = new SimpleMove(origen, destino);
		
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

		
		Move move = new CaptureMove(origen, destino);
		
		move.execute(tablero);
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e7));
		assertTrue(tablero.isEmtpy(Square.e5));
		
		move.undo(tablero);
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e7));
		
	}

}
