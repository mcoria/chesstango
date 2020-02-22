package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.CaptureMove;
import moveexecutors.CapturePeonPasante;
import moveexecutors.SaltoDoblePeonMove;
import moveexecutors.SimpleMove;
import parsers.FENBoarBuilder;

public class PeonNegroMoveGeneratorTest {
	
	private FENBoarBuilder builder;

	private PeonNegroMoveGenerator moveGenerator;

	@Before
	public void setUp() throws Exception {
		moveGenerator = new PeonNegroMoveGenerator();
		builder = new FENBoarBuilder();
	}
	
	@Test
	public void testSaltoSimple() {
		DummyBoard tablero = builder.withTablero("8/8/p7/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a6;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a5) ));
	}
	
	@Test
	public void testSaltoDoble() {		
		DummyBoard tablero = builder.withTablero("8/p7/8/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a5, Square.a6) ));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		DummyBoard tablero = builder.withTablero("8/4p3/3P4/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d6));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.a6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d6, Pieza.PEON_BLANCO) ));
	}
	
	@Test
	public void testPeonPasanteIzquierda() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/3Pp3/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d4));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		BoardState boardState = new BoardState();
		boardState.setPeonPasanteSquare(Square.d3);		
		
		tablero.setBoardState(boardState);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.d3) ));
	}
	
	@Test
	public void testAtaqueDerecha() {
		DummyBoard tablero = builder.withTablero("8/4p3/5P2/8/8/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		tablero.setBoardState(new BoardState());
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.f6));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e5, Square.e6) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f6, Pieza.PEON_BLANCO) ));
		
		assertEquals(3, moves.size());
	}
	
	@Test
	public void testPeonPasanteDerecha() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/3pP3/8/8/8").buildDummyBoard();
		
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d4;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.e4));

		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_NEGRO);
		
		BoardState boardState = new BoardState();
		boardState.setPeonPasanteSquare(Square.e3);
		
		tablero.setBoardState(boardState);
		
		Collection<Move> moves = moveGenerator.generateMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d3) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.e3) ));
	}
	
	@Test
	public void testPuedeCapturarRey() {
		DummyBoard tablero = builder.withTablero("8/8/8/8/8/8/6p1/4K2R").buildDummyBoard();
		
		assertEquals(Pieza.REY_BLANCO, tablero.getPieza(Square.e1));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h1));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.g2));

		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(Square.g2, Pieza.PEON_NEGRO);

		assertTrue( moveGenerator.puedeCapturarRey(tablero, origen, Square.f1) );
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
		return new CapturePeonPasante(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null));
	}	

}
