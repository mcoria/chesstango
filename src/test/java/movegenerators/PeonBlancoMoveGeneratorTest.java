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
import chess.Move.MoveType;
import parsers.FENParser;

public class PeonBlancoMoveGeneratorTest {

	@Test
	public void testSaltoSimple() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/P7/8/8");
		
		Square from = Square.a3;		
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(1, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.a4, MoveType.SIMPLE)));
	}
	
	@Test
	public void testSaltoDoble() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/8/P7/8");
		
		Square from = Square.a2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(2, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.a3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.a4, MoveType.SIMPLE)));
	}
	
	@Test
	public void testAtaqueIzquierda() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/3p4/4P3/8");
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.d3));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.d3, MoveType.CAPTURA)));
	}
	
	@Test
	public void testAtaqueDerecha() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/8/8/5p2/4P3/8");
		
		Square from = Square.e2;
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f3));
	
		PeonMoveGenerator moveGenerator = new PeonMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, from);
		
		assertEquals(3, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(from, Square.f3, MoveType.CAPTURA)));
	}

}
