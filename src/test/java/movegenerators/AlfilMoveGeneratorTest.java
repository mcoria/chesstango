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

public class AlfilMoveGeneratorTest {

	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4B3/8/8/8/8");

		assertEquals(Pieza.ALFIL_BLANCO, tablero.getPieza(Square.e5));

		AlfilMoveGenerator moveGenerator = new AlfilMoveGenerator(Color.BLANCO);

		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, Square.e5);

		assertEquals(13, moves.size());

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
	}

}
