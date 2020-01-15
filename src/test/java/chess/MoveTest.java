package chess;

import static org.junit.Assert.*;

import org.junit.Test;

import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class MoveTest {

	@Test
	public void testEquals01() {
		assertEquals(new Move(Square.e5, Square.e7, new SimpleMoveExecutor(Pieza.TORRE_BLANCO)), new Move(Square.e5, Square.e7, new SimpleMoveExecutor(Pieza.TORRE_BLANCO)));
	}
	
	@Test
	public void testToString01() {
		Move move = new Move(Square.e5, Square.e7, new SimpleMoveExecutor(Pieza.TORRE_BLANCO));
		assertEquals("e5 e7; Simple: TORRE_BLANCO", move.toString());
	}
	
	@Test
	public void testToString02() {
		Move move = new Move(Square.e5, Square.e7, null);
		assertEquals("e5 e7; ERROR", move.toString());
	}	
	
	
	@Test
	public void testCompare01() {
		Move move1 = new Move(Square.a2, Square.a3, null);
		Move move2 = new Move(Square.a2, Square.a4, null);
		Move move3 = new Move(Square.b2, Square.b3, null);
		Move move4 = new Move(Square.b1, Square.a3, null);
		
		assertTrue(move1.compareTo(move2) > 0);
		assertTrue(move1.compareTo(move3) > 0);
		assertTrue(move1.compareTo(move4) > 0);
		
		assertTrue(move2.compareTo(move3) > 0);
		
		assertTrue(move3.compareTo(move4) > 0);
		
		System.out.println("" + new Integer(10).compareTo(5));
	}	
	
	
	@Test
	public void testSimple() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(tablero.getPieza(Square.e5), Pieza.TORRE_BLANCO);
		
		Move move = new Move(Square.e5, Square.e7, new SimpleMoveExecutor(Pieza.TORRE_BLANCO));
		
		move.execute(tablero);
		assertEquals(tablero.getPieza(Square.e7), Pieza.TORRE_BLANCO);
		assertTrue(tablero.isEmtpy(Square.e5));
		
		move.undo(tablero);
		assertEquals(tablero.getPieza(Square.e5), Pieza.TORRE_BLANCO);
		assertTrue(tablero.isEmtpy(Square.e7));
		
	}
	
	@Test
	public void testCapture() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/4p3/8/4R3/8/8/8/8");
		
		assertEquals(tablero.getPieza(Square.e5), Pieza.TORRE_BLANCO);
		assertEquals(tablero.getPieza(Square.e7), Pieza.PEON_NEGRO);
		
		Move move = new Move(Square.e5, Square.e7, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.PEON_NEGRO));
		
		move.execute(tablero);
		assertEquals(tablero.getPieza(Square.e7), Pieza.TORRE_BLANCO);
		assertTrue(tablero.isEmtpy(Square.e5));
		
		move.undo(tablero);
		assertEquals(tablero.getPieza(Square.e5), Pieza.TORRE_BLANCO);
		assertEquals(tablero.getPieza(Square.e7), Pieza.PEON_NEGRO);
		
	}

}
