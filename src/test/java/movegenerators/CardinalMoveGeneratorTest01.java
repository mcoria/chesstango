package movegenerators;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import chess.Move.MoveType;
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8, MoveType.SIMPLE)));
	}	
	
	@Test
	public void testSur() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1, MoveType.SIMPLE)));
	}
	
	@Test
	public void testEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5, MoveType.SIMPLE)));
	}
	
	@Test
	public void testOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Oeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5, MoveType.SIMPLE)));
	}
	
	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.Este, Cardinal.Oeste, Cardinal.Norte, Cardinal.Sur});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(14, moves.size());
		
		//Norte
		assertTrue(moves.contains(new Move(Square.e5, Square.e6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8, MoveType.SIMPLE)));
		
		//Sur
		assertTrue(moves.contains(new Move(Square.e5, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1, MoveType.SIMPLE)));
		
		//Este
		assertTrue(moves.contains(new Move(Square.e5, Square.d5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5, MoveType.SIMPLE)));
		
		//Oeste
		assertTrue(moves.contains(new Move(Square.e5, Square.f5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5, MoveType.SIMPLE)));		
	}	
}
