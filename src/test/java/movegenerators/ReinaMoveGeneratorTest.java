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

public class ReinaMoveGeneratorTest {

	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4Q3/8/8/8/8");

		assertEquals(Pieza.REINA_BLANCO, tablero.getPieza(Square.e5));

		ReinaMoveGenerator moveGenerator = new ReinaMoveGenerator(Color.BLANCO);

		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);

		assertEquals(27, moves.size());

		// NorteEste
		assertTrue(moves.contains(new Move(Square.e5, Square.f6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h8)));

		// SurEste
		assertTrue(moves.contains(new Move(Square.e5, Square.f4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.g3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.h2)));

		// SurOeste
		assertTrue(moves.contains(new Move(Square.e5, Square.d4)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c3)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b2)));
		assertTrue(moves.contains(new Move(Square.e5, Square.a1)));

		// NorteOeste
		assertTrue(moves.contains(new Move(Square.e5, Square.d6)));
		assertTrue(moves.contains(new Move(Square.e5, Square.c7)));
		assertTrue(moves.contains(new Move(Square.e5, Square.b8)));
		
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
