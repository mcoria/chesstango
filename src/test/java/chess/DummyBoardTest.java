package chess;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import chess.Move.MoveType;
import parsers.FENParser;

public class DummyBoardTest {

	@Test
	public void test() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");
		
		Set<Move> moves = tablero.getPseudoMoves(Color.BLANCO);
		
		assertEquals(20, moves.size());
		
		assertTrue(moves.contains(new Move(Square.a2, Square.a3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.a2, Square.a4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.b2, Square.b3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.b2, Square.b4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.c2, Square.c3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.c2, Square.c4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.d2, Square.d3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.d2, Square.d4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e2, Square.e3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.e2, Square.e4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.f2, Square.f3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.f2, Square.f4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.g2, Square.g3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.g2, Square.g4, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.h2, Square.h3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.h2, Square.h4, MoveType.SIMPLE)));
		
		//Caballo Reyna
		assertTrue(moves.contains(new Move(Square.b1, Square.a3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.b1, Square.c3, MoveType.SIMPLE)));
		
		//Caballo Rey
		assertTrue(moves.contains(new Move(Square.g1, Square.f3, MoveType.SIMPLE)));
		assertTrue(moves.contains(new Move(Square.g1, Square.h3, MoveType.SIMPLE)));
	}

}
