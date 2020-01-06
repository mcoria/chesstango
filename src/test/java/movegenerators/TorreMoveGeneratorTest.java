package movegenerators;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import parsers.FENParser;

public class TorreMoveGeneratorTest {
	
	@Test
	public void testNorte() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getNortePseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8)));
	}
	
	@Test
	public void testSur() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getSurPseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.e4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1)));
	}
	
	@Test
	public void testEste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getEstePseudoMoves(tablero, Square.e5);
		
		assertEquals(4, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.d5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5)));
	}	
	
	@Test
	public void testOeste() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getOestePseudoMoves(tablero, Square.e5);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(Square.e5, Square.f5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5)));
	}	
	

	
	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4R3/8/8/8/8");
		
		assertEquals(Pieza.TORRE_BLANCO, tablero.getPieza(Square.e5));
	
		TorreMoveGenerator moveGenerator = new TorreMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);
		
		assertEquals(14, moves.size());
		
		//Norte
		assertTrue(moves.contains(new Move(Square.e5, Square.e6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e8)));
		
		//Sur
		assertTrue(moves.contains(new Move(Square.e5, Square.e4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.e1)));
		
		//Este
		assertTrue(moves.contains(new Move(Square.e5, Square.d5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a5)));
		
		//Oeste
		assertTrue(moves.contains(new Move(Square.e5, Square.f5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g5)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h5)));		
	}	
}
