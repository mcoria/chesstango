package movegenerators;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;

import org.junit.Test;

import chess.Color;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class ReinaMoveGeneratorTest {

	@Test
	public void testGetPseudoMoves() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/8/8/4Q3/8/8/8/8");

		Square from = Square.e5;
		assertEquals(Pieza.REINA_BLANCO, tablero.getPieza(from));

		ReinaMoveGenerator moveGenerator = new ReinaMoveGenerator(Color.BLANCO);

		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.ALFIL_BLANCO));

		assertEquals(27, moves.size());

		// NorteEste
		assertTrue(moves.contains(new Move(from, Square.f6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));

		// SurEste
		assertTrue(moves.contains(new Move(from, Square.f4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));

		// SurOeste
		assertTrue(moves.contains(new Move(from, Square.d4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a1, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));

		// NorteOeste
		assertTrue(moves.contains(new Move(from, Square.d6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Norte
		assertTrue(moves.contains(new Move(from, Square.e6, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e7, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e8, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Sur
		assertTrue(moves.contains(new Move(from, Square.e4, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e3, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e2, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.e1, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Este
		assertTrue(moves.contains(new Move(from, Square.d5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.b5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.a5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		
		//Oeste
		assertTrue(moves.contains(new Move(from, Square.f5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.h5, new SimpleMoveExecutor(Pieza.ALFIL_BLANCO))));			
	}

}
