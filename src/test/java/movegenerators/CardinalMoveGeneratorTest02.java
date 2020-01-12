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

public class CardinalMoveGeneratorTest02 {
	
	@Test
	public void testNorteEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8)));
	}
	
	
	
	@Test
	public void testNorteEste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("7R/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7)));
	}
	
	@Test
	public void testNorteEste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("7r/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8, Pieza.TORRE_NEGRO)));
	}	
	
	
	@Test
	public void testSurEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2)));
	}
	
	
	@Test
	public void testSurEste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/7R/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.h2));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3)));
	}	
	
	
	@Test
	public void testSurEste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/7r/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.h2));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2, Pieza.TORRE_NEGRO)));
	}
	

	@Test
	public void testSurOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a1)));
	}
	

	@Test
	public void testSurOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/R7");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.a1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2)));
	}	
	

	@Test
	public void testSurOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/r7");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.a1));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2)));	
		assertTrue(moves.contains(new Move(Square.e5, Square.a1, Pieza.TORRE_NEGRO)));	
	}	


	@Test
	public void testNorteOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8)));
	}
	
	
	
	@Test
	public void testNorteOeste01() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("1R6/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.b8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7)));
	}
	
	
	@Test
	public void testNorteOeste02() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("1r6/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
		assertEquals(Pieza.TORRE_NEGRO, tablero.getPieza(Square.b8));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8, Pieza.TORRE_NEGRO)));
	}
}
