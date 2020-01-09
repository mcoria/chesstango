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

public class CardinalMoveGeneratorTest02 {
	
	@Test
	public void testNorteEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8, MoveType.CAPTURA)));
	}	
	
	
	@Test
	public void testSurEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurEste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2, MoveType.CAPTURA)));
	}
	

	@Test
	public void testSurOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.SurOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a1, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2, MoveType.SIMPLE)));	
		assertTrue(moves.contains(new Move(Square.e5, Square.a1, MoveType.CAPTURA)));	
	}	


	@Test
	public void testNorteOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7, MoveType.SIMPLE)));
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
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8, MoveType.CAPTURA)));
	}

	
	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");
		
		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));
	
		CardinalMoveGenerator moveGenerator = new CardinalMoveGenerator(Color.BLANCO, new Cardinal[] {Cardinal.NorteEste, Cardinal.SurEste, Cardinal.SurOeste, Cardinal.NorteOeste});
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(13, moves.size());
		
		//NorteEste
		assertTrue(moves.contains(new Move(Square.e5, Square.f6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8, MoveType.SIMPLE)));
		
		//SurEste
		assertTrue(moves.contains(new Move(Square.e5, Square.f4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2, MoveType.SIMPLE)));
		
		//SurOeste
		assertTrue(moves.contains(new Move(Square.e5, Square.d4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a1, MoveType.SIMPLE)));
		
		//NorteOeste
		assertTrue(moves.contains(new Move(Square.e5, Square.d6, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8, MoveType.SIMPLE)));		
	}
}
