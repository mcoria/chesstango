package movegenerators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import iterators.CardinalSquareIterator.Cardinal;
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class CardinalMoveGeneratorTest01 {
	
	@Test
	public void testNorte() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	
	@Test
	public void testNorte01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4B3/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	
	@Test
	public void testNorte02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("4b3/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Norte});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e8, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO))));
	}	
	
	@Test
	public void testSur() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e1, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	
	@Test
	public void testSur01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/4B3");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	
	@Test
	public void testSur02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/4b3");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.e1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e1, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO))));
	}
	
	@Test
	public void testEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	
	@Test
	public void testEste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R2B/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.h5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	
	@Test
	public void testEste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R2b/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.h5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h5, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO))));
	}
	
	@Test
	public void testOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	
	@Test
	public void testOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/B3R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.a5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	
	@Test
	public void testOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/b3R3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.ALFIL_NEGRO, tablero.getPieza(Square.a5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.TORRE_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.TORRE_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a5, new CaptureMoveExecutor(Pieza.TORRE_BLANCO, Pieza.ALFIL_NEGRO))));
	}
		
}
