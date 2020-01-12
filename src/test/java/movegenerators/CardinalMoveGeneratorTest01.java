package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.CardinalSquareIterator.Cardinal;
import parsers.FENParser;

public class CardinalMoveGeneratorTest01 {
	
	@Test
	public void testNorte() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8)));
	}
	
	@Test
	public void testNorte01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4B3/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7)));
	}	
	
	@Test
	public void testNorte02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4b3/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8, Pieza.ALFIL_NEGRO)));
	}	
	
	@Test
	public void testSur() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1)));
	}
	
	@Test
	public void testSur01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/4B3");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2)));
	}	
	
	@Test
	public void testSur02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/4b3");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1, Pieza.ALFIL_NEGRO)));
	}
	
	@Test
	public void testEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5)));
	}	
	
	@Test
	public void testEste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R2B/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.h5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5)));
	}	
	
	@Test
	public void testEste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R2b/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.h5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5, Pieza.ALFIL_NEGRO)));
	}
	
	@Test
	public void testOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5)));
	}
	
	@Test
	public void testOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/B3R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.a5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5)));
	}	
	
	@Test
	public void testOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/b3R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.a5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5, Pieza.ALFIL_NEGRO)));
	}
		
}
