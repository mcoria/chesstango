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

public class CardinalMoveGeneratorTest02 {	
	

	@Test
	public void testSurOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a1, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	

	@Test
	public void testSurOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/R7");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}	
	

	@Test
	public void testSurOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/r7");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));	
		assertTrue(moves.contains(new Move(from, Square.a1, new CaptureMoveExecutor(Pieza.ALFIL_BLANCO, Pieza.TORRE_NEGRO))));	
	}	


	@Test
	public void testNorteOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	
	
	
	@Test
	public void testNorteOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("1R6/8/8/4B3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.b8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
	}
	
	
	@Test
	public void testNorteOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("1r6/8/8/4B3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.b8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.d6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b8, new CaptureMoveExecutor(Pieza.ALFIL_BLANCO, Pieza.TORRE_NEGRO))));
	}
}
