package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import chess.CachePosiciones;
import chess.Board;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.EnroqueBlancoReyMove;
import moveexecutors.EnroqueBlancoReynaMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class ReyBlancoMoveGeneratorTest {

	private FENBoarBuilder builder;
	
	private ReyBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new ReyBlancoMoveGenerator();
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void test01() {
		Board tablero = builder.withTablero("8/8/8/4K3/8/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(8, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}

	@Test
	public void test02() {
		Board tablero = builder.withTablero("8/8/4P3/4K3/4p3/8/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(from));		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e6));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f6) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f5) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.e4, Pieza.PEON_NEGRO) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f4) ));			
	}
	
	
	@Test
	public void testEnroqueBlancoReina01() {
		Board tablero = 
				builder
				.withTablero("8/8/8/8/8/8/8/R3K3")
				.withEnroqueBlancoReinaPermitido(true)
				.buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(6, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoReynaMove() ));
	}
	
	@Test
	public void testEnroqueBlancoReina02() {
		Board tablero = 
				builder
				.withTablero("8/8/8/8/8/5b2/8/R3K3")
				.withEnroqueBlancoReinaPermitido(true)
				.buildDummyBoard();
		moveGenerator.setTablero(tablero);

		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueBlancoReina03() {
		Board tablero = builder.withTablero("8/8/8/8/5b2/8/8/R3K3").withEnroqueBlancoReinaPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.f4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());
	}
	
	@Test
	public void testEnroqueBlancoReina04() {
		Board tablero = builder.withTablero("8/8/8/8/8/8/8/RN2K3").withEnroqueBlancoReinaPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(Square.b1));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertFalse(moves.contains( new EnroqueBlancoReynaMove() ));
		
		assertEquals(5, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoRey01() {
		Board tablero = builder.withTablero("8/8/8/8/8/8/8/4K2R").withEnroqueBlancoReyPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		assertTrue(moves.contains( new EnroqueBlancoReyMove() ));
		
		assertEquals(6, moves.size());
	}	
	
	@Test
	public void testEnroqueBlancoRey02() {
		Board tablero = builder.withTablero("8/8/8/8/8/3b4/8/4K2R").withEnroqueBlancoReyPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(5, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
	}
	
	@Test
	public void testEnroqueBlancoRey03() {
		Board tablero = builder.withTablero("8/8/8/8/3b4/8/8/4K2R").withEnroqueBlancoReyPermitido(true).buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.d4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());
	}		
	
	@Test
	public void testEnroqueBlancoRey04() {
		Board tablero = builder.withTablero("8/8/8/8/8/8/6p1/4K2R").withEnroqueBlancoReyPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.e2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		
		assertEquals(4, moves.size());
	}		

	@Test
	public void testEnroqueBlancoJaque() {
		Board tablero = builder.withTablero("8/8/8/8/4r3/8/8/R3K2R").withEnroqueBlancoReinaPermitido(true).buildDummyBoard();
		moveGenerator.setTablero(tablero);
		moveGenerator.setFilter(tablero.getDefaultFilter());
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(CachePosiciones.REY_BLANCO.getKey()));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.e4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(CachePosiciones.REY_BLANCO.getKey(), Pieza.REY_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d1) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.d2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f2) ));
		assertTrue(moves.contains( createSimpleMove(origen, Square.f1) ));
		
		assertEquals(4, moves.size());		
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new SimpleMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza));
	}	
	
}
