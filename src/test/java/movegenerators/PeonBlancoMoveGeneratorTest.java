package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class PeonBlancoMoveGeneratorTest {
	
	private FENBoarBuilder builder;
	
	private PeonBlancoMoveGenerator moveGenerator;
	
	private Collection<Move> moves; 

	@Before
	public void setUp() throws Exception {
		builder = new FENBoarBuilder();
		moveGenerator = new PeonBlancoMoveGenerator();
		moves = new ArrayList<Move>();
	}
	
	@Test
	public void testSaltoSimple() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/P7/8/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/8/P7/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4, Square.a3) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/N7/P7/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/3p4/4P3/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteIzquierda() {
		DummyBoard tablero = 
				builder
				.withTablero("8/8/8/3pP3/8/8/8/8")
				.withPeonPasanteSquare(Square.d6)
				.buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d5));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.d6) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/5p2/4P3/8").buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4, Square.a3) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteDerecha() {
		DummyBoard tablero = 
				builder
				.withTablero("8/8/8/3Pp3/8/8/8/8")
				.withPeonPasanteSquare(Square.e6)
				.buildDummyBoard();
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e5));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		moveGenerator.generateMoves(origen, moves);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.e6) ));
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new SimpleMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}
	
	private Move createSaltoDobleMove(Entry<Square, Pieza> origen, Square destinoSquare, Square squarePasante) {
		return new SaltoDoblePeonMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), squarePasante);
	}	
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new CaptureMove(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza));
	}

	private Move createCapturePeonPasanteMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new CapturePeonPasante(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), new SimpleImmutableEntry<Square, Pieza>(Square.getSquare(destinoSquare.getFile(), 4), Pieza.PEON_NEGRO));
	}	
	
}
