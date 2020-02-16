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
import chess.Move.MoveType;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class PeonBlancoMoveGeneratorTest {

	private PeonBlancoMoveGenerator moveGenerator;

	@Before
	public void setUp() throws Exception {
		moveGenerator = new PeonBlancoMoveGenerator();
	}
	
	@Test
	public void testSaltoSimple() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/P7/8/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a3;		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/P7/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.a3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.a4) ));
	}
	
	@Test
	public void testSaltoDoble01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/N7/P7/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(0, moves.size());
		

	}	
	
	@Test
	public void testAtaqueIzquierda() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/3p4/4P3/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.d3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteIzquierda() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/3pP3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d5));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		BoardState boardState = new BoardState();
		boardState.setPeonPasanteSquare(Square.d6);		
		
		tablero.setBoardState(boardState);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.d6) ));
	}
	
	
	@Test
	public void testAtaqueDerecha() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/5p2/4P3/8");
		tablero.setBoardState(new BoardState());
		moveGenerator.setTablero(tablero);
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f3));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.e3) ));
		assertTrue(moves.contains( createSaltoDobleMove(origen, Square.e4) ));
		assertTrue(moves.contains( createCaptureMove(origen, Square.f3, Pieza.PEON_NEGRO) ));
	}
	
	@Test
	public void testPeonPasanteDerecha() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/3Pp3/8/8/8/8");
		moveGenerator.setTablero(tablero);
		
		Square from = Square.d5;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.e5));
		
		Map.Entry<Square, Pieza> origen = new SimpleImmutableEntry<Square, Pieza>(from, Pieza.PEON_BLANCO);
		
		BoardState boardState = new BoardState();
		boardState.setPeonPasanteSquare(Square.e6);
		
		tablero.setBoardState(boardState);
		
		Collection<Move> moves = moveGenerator.getPseudoMoves(origen);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains( createSimpleMove(origen, Square.d6) ));
		assertTrue(moves.contains( createCapturePeonPasanteMove(origen, Square.e6) ));
	}
	
	private Move createSimpleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SIMPLE);
	}
	
	private Move createSaltoDobleMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.SALTO_DOBLE_PEON);
	}	
	
	private Move createCaptureMove(Entry<Square, Pieza> origen, Square destinoSquare, Pieza destinoPieza) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, destinoPieza), MoveType.CAPTURA);
	}

	private Move createCapturePeonPasanteMove(Entry<Square, Pieza> origen, Square destinoSquare) {
		return new Move(origen, new SimpleImmutableEntry<Square, Pieza>(destinoSquare, null), MoveType.CAPTURA_PEON_PASANTE);
	}	
	
}
