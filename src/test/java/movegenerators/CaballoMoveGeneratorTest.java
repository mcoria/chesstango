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
import moveexecutors.CaptureMoveExecutor;
import moveexecutors.SimpleMoveExecutor;
import parsers.FENParser;

public class CaballoMoveGeneratorTest {

	@Test
	public void test() {
		FENParser parser = new FENParser();
		DummyBoard tablero = parser.parsePiecePlacement("8/3P1p2/8/4N3/8/8/8/8");
		
		Square from = Square.e5;
		assertEquals(Pieza.CABALLO_BLANCO, tablero.getPieza(from));
		assertEquals(Pieza.PEON_BLANCO, tablero.getPieza(Square.d7));
		assertEquals(Pieza.PEON_NEGRO, tablero.getPieza(Square.f7));		
	
		CaballoMoveGenerator moveGenerator = new CaballoMoveGenerator(Color.BLANCO);
		
		Set<Move> moves = moveGenerator.getPseudoMoves(tablero, new SimpleImmutableEntry<Square, Pieza>(from, Pieza.CABALLO_BLANCO));
		
		assertEquals(7, moves.size());
		
		assertTrue(moves.contains(new Move(from, Square.g6, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.g4, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.f3, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.d3, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c4, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		assertTrue(moves.contains(new Move(from, Square.c6, new SimpleMoveExecutor(Pieza.CABALLO_BLANCO))));
		// Peon Blanco en d7
		assertTrue(moves.contains(new Move(from, Square.f7, new CaptureMoveExecutor(Pieza.CABALLO_BLANCO, Pieza.PEON_NEGRO))));
	}

}
