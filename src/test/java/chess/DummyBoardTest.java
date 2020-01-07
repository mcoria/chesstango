package chess;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import parsers.FENParser;

public class DummyBoardTest {

	@Test
	public void test() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		Set<Move> moves = tablero.getPseudoMoves(Color.BLANCO);
		
		assertEquals(16, moves.size());
		
		assertTrue(moves.contains(new Move(Square.a2, Square.a3)));
		assertTrue(moves.contains(new Move(Square.a2, Square.a4)));
		assertTrue(moves.contains(new Move(Square.b2, Square.b3)));
		assertTrue(moves.contains(new Move(Square.b2, Square.b4)));
		assertTrue(moves.contains(new Move(Square.c2, Square.c3)));
		assertTrue(moves.contains(new Move(Square.c2, Square.c4)));
		assertTrue(moves.contains(new Move(Square.d2, Square.d3)));
		assertTrue(moves.contains(new Move(Square.d2, Square.d4)));
		assertTrue(moves.contains(new Move(Square.e2, Square.e3)));
		assertTrue(moves.contains(new Move(Square.e2, Square.e4)));
		assertTrue(moves.contains(new Move(Square.f2, Square.f3)));
		assertTrue(moves.contains(new Move(Square.f2, Square.f4)));
		assertTrue(moves.contains(new Move(Square.g2, Square.g3)));
		assertTrue(moves.contains(new Move(Square.g2, Square.g4)));
		assertTrue(moves.contains(new Move(Square.h2, Square.h3)));
		assertTrue(moves.contains(new Move(Square.h2, Square.h4)));
	}

}
