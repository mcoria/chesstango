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
import parsers.FENParser;

public class PeonNegroMoveGeneratorTest02 {

	@Test
	public void testSaltoSimple() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/p7/8/8/8/8/8");
		
		Square from = Square.a6;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.NEGRO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.a5)));
	}
	
	@Test
	public void testSaltoDoble() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/p7/8/8/8/8/8/8");
		
		Square from = Square.a7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.NEGRO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.a6)));
		assertTrue(moves.contains(new Move(from, Square.a5)));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/4p3/3P4/8/8/8/8/8");
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d6));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.NEGRO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6)));
		assertTrue(moves.contains(new Move(from, Square.e5)));
		assertTrue(moves.contains(new Move(from, Square.d6, Pieza.PEON_BLANCO)));
	}
	
	@Test
	public void testAtaqueDerecha() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/4p3/5P2/8/8/8/8/8");
		
		Square from = Square.e7;
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.f6));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.NEGRO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e6)));
		assertTrue(moves.contains(new Move(from, Square.e5)));
		assertTrue(moves.contains(new Move(from, Square.f6, Pieza.PEON_BLANCO)));
	}

}
